
package com.ark.rule.platform.biz.biz;

import com.alibaba.fastjson.JSON;
import com.ark.rule.platform.api.Result;
import com.ark.rule.platform.api.domain.fg.RuleReqDTO;
import com.ark.rule.platform.api.domain.fg.request.BatchExecuteReqDTO;
import com.ark.rule.platform.api.domain.fg.request.ExecuteItemDTO;
import com.ark.rule.platform.api.domain.fg.request.ExecuteReqDTO;
import com.ark.rule.platform.api.domain.fg.response.ExecuteResultDTO;
import com.ark.rule.platform.api.domain.fg.response.HitRuleDTO;
import com.ark.rule.platform.api.service.fg.IRuleService;
import com.ark.rule.platform.common.enums.MetaValueTypeEnum;
import com.ark.rule.platform.domain.dto.inner.IBusinessDTO;
import com.ark.rule.platform.domain.dto.inner.IGroupRuleDTO;
import com.ark.rule.platform.domain.dto.inner.IMetaDTO;
import com.ark.rule.platform.domain.dto.inner.IRuleDTO;
import com.ark.rule.platform.domain.dto.inner.IRuleMetaDTO;
import com.ark.rule.platform.domain.dto.inner.IRuleResultDTO;
import com.ark.rule.platform.domain.dto.inner.IRuleTemplateMetaDTO;
import com.ark.rule.platform.domain.enums.ResultEnum;
import com.ark.rule.platform.domain.exception.TransException;
import com.ark.rule.platform.domain.service.bo.ExecutorEntity;
import com.ark.rule.platform.domain.service.bo.MetaExecuteBO;
import com.ark.rule.platform.domain.service.config.RuleCommonConfig;
import com.ark.rule.platform.domain.service.guava.IBusinessGuavaService;
import com.ark.rule.platform.domain.service.guava.IGroupGuavaService;
import com.ark.rule.platform.domain.service.guava.IMetaGuavaService;
import com.ark.rule.platform.domain.service.guava.IRuleGuavaService;
import com.ark.rule.platform.domain.service.guava.IRuleMetaGuavaService;
import com.ark.rule.platform.domain.service.guava.IRuleResultGuavaService;
import com.ark.rule.platform.domain.service.guava.ITemplateMetaGuavaService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 描述类的功能.
 *
 */
@Service("ruleService")
public class RuleServiceImpl implements IRuleService {
    private static final Logger logger = LoggerFactory.getLogger(RuleServiceImpl.class);

    @Resource
    private IBusinessGuavaService businessGuavaService;
    @Resource
    private IGroupGuavaService groupGuavaService;

    @Resource
    private IRuleGuavaService ruleGuavaService;

    @Resource
    private IMetaGuavaService metaGuavaService;

    @Resource
    private IRuleResultGuavaService ruleResultGuavaService;

    @Resource
    private IRuleMetaGuavaService ruleMetaGuavaService;

    @Resource
    private ITemplateMetaGuavaService templateMetaGuavaService;

    @Resource
    private RuleCommonConfig ruleCommonConfig;

    @Override
    public Result<HitRuleDTO> execute(ExecuteReqDTO execute) {
        HitRuleDTO hitRuleDTO = null;
        try {
            // 参数校验
            checkSingleParam(execute);

            // 构造上下文
            ExecutorEntity entity = gainExecuteEntity(execute.getGroupId(), execute.getMetas());
            // 执行
            hitRuleDTO = executeAndGetResult(entity);
            return Result.wrapSuccess(hitRuleDTO);
        } catch (TransException e) {
            logger.info("execute 内部异常,类型:{}, 错误信息:{}", e.getBizErrorMsg(), e.getPrintErrorMsg());
            return Result.wrapError(e.getErrorCode(), e.getPrintErrorMsg());
        } catch (Exception e) {
            logger.error("execute request param:{}, unknown e:", JSON.toJSONString(execute), e);
            return Result.wrapError(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMsg());
        } finally {
            logger.info("execute入参:{}, 出参:{}", JSON.toJSONString(execute), JSON.toJSONString(hitRuleDTO));
        }
    }

    @Override
    public Result<List<ExecuteResultDTO>> batchExecute(BatchExecuteReqDTO req) {
        List<ExecuteResultDTO> executeResultList = Lists.newArrayList();
        try {
            //参数校验
            checkParam(req);
            //组装参数并执行
            for (ExecuteItemDTO item : req.getExecuteItems()) {
                if (item == null) {
                    continue;
                }
                //构造上下文
                ExecutorEntity entity = gainExecuteEntity(item.getGroupId(), item.getMetas());
                //执行并获得规则结果
                ExecuteResultDTO executeResultDTO =
                        ExecuteResultDTO.builder().groupId(item.getGroupId()).build();
                HitRuleDTO hitRule = executeAndGetResult(entity);
                if (hitRule != null) {
                    executeResultDTO.setHitRuleId(hitRule.getHitRuleId());
                    executeResultDTO.setResult(hitRule.getResult());
                }
                executeResultList.add(executeResultDTO);
            }
            return Result.wrapSuccess(executeResultList);
        } catch (TransException e) {
            logger.info("batchExecute 内部异常,类型:{}, 错误信息:{}", e.getBizErrorMsg(), e.getPrintErrorMsg());
            return Result.wrapError(e.getErrorCode(), e.getPrintErrorMsg());
        } catch (Exception e) {
            logger.error("batchExecute request param:{}, unknown e:", JSON.toJSONString(req), e);
            return Result.wrapError(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMsg());
        } finally {
            logger.info("batchExecute入参:{}, 出参:{}", JSON.toJSONString(req), JSON.toJSONString(executeResultList));
        }
    }

    /**
     * 单个计算参数验证.
     *
     * @param execute ''
     */
    private void checkSingleParam(ExecuteReqDTO execute) {
        //验证参数非空
        checkNullParam(execute);
        //参数业务校验
        singleBizCheck(execute);
    }

    /**
     * 单个执行 业务校验.
     *
     * @param execute ''
     */
    private void singleBizCheck(ExecuteReqDTO execute) {
        checkBizCode(execute.getBizCode());
        checkGroup(execute.getGroupId(), execute.getMetas());
    }

    /**
     * 单个接口参数非空验证.
     *
     * @param execute ''
     */
    private void checkNullParam(ExecuteReqDTO execute) {
        if (execute.getGroupId() == null) {
            throw new TransException(ResultEnum.PARAM_ERROR, "groupId不能为空");
        }
        if (StringUtils.isBlank(execute.getBizCode())) {
            throw new TransException(ResultEnum.PARAM_ERROR, "bizCode不能为空");
        }
        if (CollectionUtils.isEmpty(execute.getMetas())) {
            throw new TransException(ResultEnum.PARAM_ERROR, "元数据请求值不能为空");
        }
        for (RuleReqDTO reqDTO : execute.getMetas()) {
            if (reqDTO == null) {
                continue;
            }
            checkRuleReq(reqDTO, execute.getGroupId());
        }
    }

    /**
     * 执行并获取执行结果.
     *
     * @param entity 执行器
     * @return ''
     */
    private HitRuleDTO executeAndGetResult(ExecutorEntity entity) {
        Long ruleId = entity.execute();
        logger.info("groupId:{} 命中ruleId:{}", entity.getGroupId(), ruleId);
        if (ruleId == null) {
            return null;
        }
        HitRuleDTO hitRule = HitRuleDTO.builder().hitRuleId(ruleId).build();
        IRuleResultDTO ruleResult = ruleResultGuavaService.getResultByRuleId(ruleId);
        if (ruleResult != null) {
            hitRule.setResult(ruleResult.getResult());
        }
        return hitRule;
    }

    /**
     * 验证入参.
     *
     * @param req 入参
     * @return ''
     */
    private void checkParam(BatchExecuteReqDTO req) {
        //入参基本校验
        checkParamNotNull(req);
        //参数业务校验
        paramBusinessCheck(req);
    }

    /**
     * 校验入参非空.
     *
     * @param req 入参
     */
    private void checkParamNotNull(BatchExecuteReqDTO req) {
        if (StringUtils.isBlank(req.getBizCode())) {
            throw new TransException(ResultEnum.PARAM_ERROR, "bizCode不能为空");
        }
        if (CollectionUtils.isEmpty(req.getExecuteItems())) {
            throw new TransException(ResultEnum.PARAM_ERROR, "executeItems不能为空");
        }
        //验证规则组个数
        if (req.getExecuteItems().size() > ruleCommonConfig.getBatchExecutorNumLimit()) {
            throw new TransException(ResultEnum.PARAM_ERROR, "规则组个数超限,限制个数:"
                    + ruleCommonConfig.getBatchExecutorNumLimit());
        }
        for (ExecuteItemDTO item : req.getExecuteItems()) {
            if (item == null) {
                continue;
            }
            checkExecuteItem(item);
        }
    }

    /**
     * 校验规则组请求参数.
     *
     * @param item ''
     */
    private void checkExecuteItem(ExecuteItemDTO item) {
        if (item.getGroupId() == null) {
            throw new TransException(ResultEnum.PARAM_ERROR, "groupId不能为空");
        }
        if (CollectionUtils.isEmpty(item.getMetas())) {
            throw new TransException(ResultEnum.PARAM_ERROR,
                    "groupId:" + item.getGroupId() + "元数据请求值不能为空");
        }
        for (RuleReqDTO reqDTO : item.getMetas()) {
            if (reqDTO == null) {
                continue;
            }
            checkRuleReq(reqDTO, item.getGroupId());
        }
    }

    /**
     * 校验元数据请求参数.
     *
     * @param reqDTO  ''
     * @param groupId 规则组id
     */
    private void checkRuleReq(RuleReqDTO reqDTO, Long groupId) {
        if (StringUtils.isBlank(reqDTO.getCode())) {
            throw new TransException(ResultEnum.PARAM_ERROR,
                    "groupId:" + groupId + "元数据code不能为空");
        }
    }

    /**
     * 参数业务校验.
     *
     * @param req 入参
     * @return ''
     */
    private void paramBusinessCheck(BatchExecuteReqDTO req) {
        checkBizCode(req.getBizCode());
        //验证规则组
        for (ExecuteItemDTO item : req.getExecuteItems()) {
            checkGroup(item.getGroupId(), item.getMetas());
        }
    }

    /**
     * 业务线code验证
     *
     * @param bizCode code
     */
    private void checkBizCode(String bizCode) {
        //查询并验证业务线.
        IBusinessDTO business = businessGuavaService.getBusinessByBizCode(bizCode);
        if (business == null) {
            logger.info("bizCode:{} 对应业务线不存在.", bizCode);
            throw new TransException(ResultEnum.NO_BUSINESS, bizCode + " 业务线不存在");
        }
    }

    /**
     * 构造上下文.
     *
     * @param groupId     规则组id
     * @param metaReqList 元数据请求入参.
     * @return ''
     */
    private ExecutorEntity gainExecuteEntity(Long groupId, List<RuleReqDTO> metaReqList) {
        //获取规则组信息
        IGroupRuleDTO group = groupGuavaService.getGroupById(groupId);
        //获取规则组下所有规则
        List<IRuleDTO> groupRule = ruleGuavaService.getGroupRuleByGroupId(groupId);
        //获取规则组下所有规则元数据配置信息
        List<IRuleMetaDTO> ruleMetas = getRuleMetas(groupRule);
        //获取规则计算元数据对象.
        List<MetaExecuteBO> executeMetas = getExecutorMetaBO(groupId, group.getTemplateId(), metaReqList);
        Boolean alphaBetaFlag = ruleCommonConfig.getExecutorAlphaBetaFilter() == 1;
        return ExecutorEntity.newExecuteInstance(group, groupRule, ruleMetas, executeMetas, alphaBetaFlag);
    }

    /**
     * 获取规则计算元数据数据.
     *
     * @param groupId     规则组id
     * @param templateId  模板id
     * @param metaReqList 请求参数
     * @return ''
     */
    private List<MetaExecuteBO> getExecutorMetaBO(Long groupId, Long templateId, List<RuleReqDTO> metaReqList) {
        //获取模板对应元数据列表.
        List<IRuleTemplateMetaDTO> templateMetas = templateMetaGuavaService.getTemplateMetaByTid(templateId);
        if (CollectionUtils.isEmpty(templateMetas)) {
            return Lists.newArrayList();
        }
        //获取规则组元数据值对应规则ids 剪枝算法使用.
        Map<String, Map<String, List<Long>>> metaValueRuleMap = getMetaValueRuleMap(groupId);
        //获取元数据请求参数
        Map<String, String> ruleReqMap = getMetaReqMap(metaReqList);
        List<MetaExecuteBO> metaExecuteBOList = Lists.newArrayList();
        for (IRuleTemplateMetaDTO templateMetaDTO : templateMetas) {
            String metaCode = templateMetaDTO.getMetaCode();
            MetaExecuteBO metaExecuteBO = MetaExecuteBO.builder().metaCode(metaCode)
                    .operator(templateMetaDTO.getOperator())
                    .reqValue(ruleReqMap.get(metaCode))
                    .metaValueRuleMap(metaValueRuleMap.get(metaCode)).build();
            IMetaDTO meta = metaGuavaService.getMetaByMetaCode(metaCode);
            if (meta == null) {
                throw new RuntimeException(metaCode + " 元数据未查询到配置信息");
            }
            metaExecuteBO.setDefaultValue(meta.getDefaultValue());
            metaExecuteBOList.add(metaExecuteBO);
        }
        return metaExecuteBOList;
    }

    /**
     * list转map.
     * lambda表达式转换value不能为null,需要给默认值.
     *
     * @param metaReqList 元数据请求列表.
     * @return ''
     */
    private Map<String, String> getMetaReqMap(List<RuleReqDTO> metaReqList) {
        Map<String, String> reqMetaMap = Maps.newHashMap();
        for (RuleReqDTO ruleReqDTO : metaReqList) {
            reqMetaMap.put(ruleReqDTO.getCode(), ruleReqDTO.getValue());
        }
        return reqMetaMap;
    }

    private Map<String, Map<String, List<Long>>> getMetaValueRuleMap(Long groupId) {
        if (ruleCommonConfig.getExecutorAlphaBetaFilter() != 1) {
            return Maps.newHashMap();
        }
        return ruleGuavaService.getMetaValueRuleIds(groupId);
    }


    /**
     * 验证规则组信息.
     *
     * @param groupId 规则组id
     * @param metas   元数据请求值
     */
    private void checkGroup(Long groupId, List<RuleReqDTO> metas) {
        //检测规则组是否存在.
        IGroupRuleDTO group = checkGroupBiz(groupId);
        //获取规则组对应元数据
        List<IRuleTemplateMetaDTO> templateMetas =
                templateMetaGuavaService.getTemplateMetaByTid(group.getTemplateId());
        if (CollectionUtils.isEmpty(templateMetas)) {
            throw new RuntimeException(groupId + " 规则模板对应元数据不存在");
        }
        Map<String, String> groupReqParam = getMetaReqMap(metas);
        //检验入参元数据参数完整性及类型
        for (IRuleTemplateMetaDTO templateMeta : templateMetas) {
            if (templateMeta == null) {
                continue;
            }
            if (!groupReqParam.containsKey(templateMeta.getMetaCode())) {
                throw new TransException(ResultEnum.PARAM_ERROR,
                        "groupId:" + groupId + "元数据Code:" + templateMeta.getMetaCode() + " 缺失");
            }
            String value = groupReqParam.get(templateMeta.getMetaCode());
            //检验参数类型是否合法
            IMetaDTO meta = metaGuavaService.getMetaByMetaCode(templateMeta.getMetaCode());
            if (meta == null) {
                throw new RuntimeException(templateMeta.getMetaCode() + " 对应元数据配置不存在");
            }
            MetaValueTypeEnum valueType = MetaValueTypeEnum.getValueTypeByCode(meta.getValueType());
            if (valueType == null) {
                throw new RuntimeException(templateMeta.getMetaCode() + " 对应元数据配置数据类型不存在");
            }
            if (valueType == MetaValueTypeEnum.NUMBER && StringUtils.isNotBlank(value)
                    && !NumberUtils.isNumber(value)) {
                logger.info("groupId:{}, metaCode:{} 值类型应为number, value:{}",
                        groupId, templateMeta.getMetaCode(), value);
                throw new TransException(ResultEnum.PARAM_TYPE_ERROR,
                        String.format("groupId:%s, metaCode:%s 值类型应为number, value:%s",
                                groupId, templateMeta.getMetaCode(), value));
            }
        }
    }

    /**
     * 验证业务规则.
     *
     * @param groupId ''
     * @return ''
     */
    private IGroupRuleDTO checkGroupBiz(Long groupId) {
        IGroupRuleDTO group = groupGuavaService.getGroupById(groupId);
        if (group == null) {
            throw new TransException(ResultEnum.NO_RULE_GROUP, groupId + " 规则组不存在");
        }
        return group;
    }

    /**
     * 获取规则下的元数据信息.
     *
     * @param rules ""
     * @return ""
     */
    private List<IRuleMetaDTO> getRuleMetas(List<IRuleDTO> rules) {
        List<Long> ruleIds = rules.stream().map(IRuleDTO::getId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(ruleIds)) {
            return Lists.newArrayList();
        }
        return ruleMetaGuavaService.getRuleMetaByRuleIds(ruleIds);
    }
}
