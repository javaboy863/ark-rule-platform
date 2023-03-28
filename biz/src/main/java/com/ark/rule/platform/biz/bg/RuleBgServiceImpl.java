
package com.ark.rule.platform.biz.bg;

import com.alibaba.fastjson.JSON;
import com.ark.rule.platform.api.Result;
import com.ark.rule.platform.api.domain.bg.DelGroupRuleDTO;
import com.ark.rule.platform.api.domain.bg.RuleDTO;
import com.ark.rule.platform.api.domain.bg.RuleGroupDTO;
import com.ark.rule.platform.api.domain.bg.RuleMetaDTO;
import com.ark.rule.platform.api.service.bg.IRuleBgService;
import com.ark.rule.platform.biz.bo.SaveGroupCheckResBO;
import com.ark.rule.platform.common.constant.RuleConstant;
import com.ark.rule.platform.common.enums.MetaValueTypeEnum;
import com.ark.rule.platform.common.enums.OperatorEnum;
import com.ark.rule.platform.common.util.BeanConvertUtil;
import com.ark.rule.platform.domain.dao.GroupRuleDao;
import com.ark.rule.platform.domain.dao.domain.GroupRuleDOExample;
import com.ark.rule.platform.domain.dto.req.AddRuleGroupReqDTO;
import com.ark.rule.platform.domain.dto.req.DelGroupRuleReqDTO;
import com.ark.rule.platform.domain.dto.req.RuleMetaReqDTO;
import com.ark.rule.platform.domain.dto.req.RuleReqDTO;
import com.ark.rule.platform.domain.dto.req.UpdateRuleGroupReqDTO;
import com.ark.rule.platform.domain.dto.response.BusinessBaseResDTO;
import com.ark.rule.platform.domain.dto.response.GroupRuleBaseResDTO;
import com.ark.rule.platform.domain.dto.response.MetaBaseDTO;
import com.ark.rule.platform.domain.dto.response.RuleBaseDTO;
import com.ark.rule.platform.domain.dto.response.RuleMetaBaseDTO;
import com.ark.rule.platform.domain.dto.response.RuleResultBaseDTO;
import com.ark.rule.platform.domain.dto.response.RuleTemplateBaseResDTO;
import com.ark.rule.platform.domain.dto.response.TemplateMetaBaseResDTO;
import com.ark.rule.platform.domain.enums.ResultEnum;
import com.ark.rule.platform.domain.exception.TransException;
import com.ark.rule.platform.domain.service.bg.RuleBgFacadeService;
import com.ark.rule.platform.domain.service.config.RuleCommonConfig;
import com.ark.rule.platform.domain.service.db.IBusinessDbService;
import com.ark.rule.platform.domain.service.db.IGroupRuleDbService;
import com.ark.rule.platform.domain.service.db.IMetaDbService;
import com.ark.rule.platform.domain.service.db.IRuleDbService;
import com.ark.rule.platform.domain.service.db.IRuleMetaDbService;
import com.ark.rule.platform.domain.service.db.IRuleResultDbService;
import com.ark.rule.platform.domain.service.db.IRuleTemplateDbService;
import com.ark.rule.platform.domain.service.db.ITemplateMetaDbService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 规则B端增删改查服务实现.
 *
 */
@Service("ruleBgService")
public class RuleBgServiceImpl implements IRuleBgService {
    private static final Logger logger = LoggerFactory.getLogger(RuleBgServiceImpl.class);

    @Resource
    private RuleBgFacadeService ruleBgFacadeService;

    @Resource
    private IGroupRuleDbService groupRuleDbService;

    @Resource
    private IRuleTemplateDbService ruleTemplateDbService;

    @Resource
    private IBusinessDbService businessDbService;

    @Resource
    private ITemplateMetaDbService templateMetaDbService;

    @Resource
    private IMetaDbService metaDbService;

    @Resource
    private IRuleDbService ruleDbService;

    @Resource
    private IRuleMetaDbService ruleMetaDbService;

    @Resource
    private IRuleResultDbService ruleResultDbService;

    @Resource
    private RuleCommonConfig ruleCommonConfig;
    @Resource
    private GroupRuleDao groupRuleDao;
    /**
     * 规则元数据value和result长度限制
     */
    private static final int MAX_FIELD_SIZE = 4000;

    private static final int MAX_RESULT_SIZE = 1000;
    /**
     * 规则组名和规则名长度限制
     */
    private static final int MAX_NAME_SIZE = 100;
    /**
     * 规则优先级值
     */
    private static final int MAX_PRIORITY = 99999;
    /**
     * 操作人限制长度.
     */
    private static final int MAX_USER_SIZE = 50;
    /**
     * between配置参数长度.
     */
    private static final int PARAM_SIZE = 2;

    @Override
    public Result<Long> saveRuleGroup(RuleGroupDTO req) {
        Long groupId = null;
        try {
            //参数校验
            SaveGroupCheckResBO saveGroupRuleBO = checkParam(req);
            if (req.getId() == null) {
                //组装新增参数
                AddRuleGroupReqDTO addRuleGroupReqDTO = gainAddRuleGroup(req, saveGroupRuleBO);
                //新增规则组
                groupId = ruleBgFacadeService.addRuleGroup(addRuleGroupReqDTO);
                return Result.wrapSuccess(groupId);
            }
            //组装更新参数
            UpdateRuleGroupReqDTO updateRuleGroupReqDTO = gainUpdateRuleGroup(req);
            //更新规则组
            ruleBgFacadeService.updateRuleGroup(updateRuleGroupReqDTO);
            groupId = req.getId();
            return Result.wrapSuccess(groupId);
        } catch (TransException e) {
            logger.info("saveRuleGroup 内部异常,类型:{}, 错误信息:{}", e.getBizErrorMsg(), e.getPrintErrorMsg());
            return Result.wrapError(e.getErrorCode(), e.getPrintErrorMsg());
        } catch (Exception e) {
            logger.error("saveRuleGroup request param:{}, unknown e:", JSON.toJSONString(req), e);
            return Result.wrapError(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMsg());
        } finally {
            logger.info("saveRuleGroup入参:{}, 出参:{}", JSON.toJSONString(req), JSON.toJSONString(groupId));
        }
    }

    @Override
    public Result deleteRuleGroupById(DelGroupRuleDTO group) {
        try {
            checkDelParam(group);
            //组装参数
            DelGroupRuleReqDTO reqDTO = BeanConvertUtil.conver(group, DelGroupRuleReqDTO.class);
            ruleBgFacadeService.deleteGroupRule(reqDTO);
            return Result.wrapSuccess(null);
        } catch (TransException e) {
            logger.info("deleteRuleGroupById 内部异常,类型:{}, 错误信息:{}", e.getBizErrorMsg(), e.getPrintErrorMsg());
            return Result.wrapError(e.getErrorCode(), e.getPrintErrorMsg());
        } catch (Exception e) {
            logger.error("deleteRuleGroupById request param:{}, unknown e:", JSON.toJSONString(group), e);
            return Result.wrapError(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMsg());
        } finally {
            logger.info("deleteRuleGroupById入参:{}", JSON.toJSONString(group));
        }
    }

    @Override
    public Result<RuleGroupDTO> queryRuleGroupById(Long groupId) {
        RuleGroupDTO ruleGroupDTO = null;
        try {
            checkQueryParam(groupId);

            ruleGroupDTO = queryAndAssembleGroup(groupId);

            return Result.wrapSuccess(ruleGroupDTO);
        } catch (TransException e) {
            logger.info("queryRuleGroupById 内部异常,类型:{}, 错误信息:{}", e.getBizErrorMsg(), e.getPrintErrorMsg());
            return Result.wrapError(e.getErrorCode(), e.getPrintErrorMsg());
        } catch (Exception e) {
            logger.error("queryRuleGroupById request param:{}, unknown e:", groupId, e);
            return Result.wrapError(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMsg());
        } finally {
            logger.info("queryRuleGroupById入参:{}, 出参:{}", groupId, JSON.toJSONString(ruleGroupDTO));
        }
    }

    @Override
    public boolean isUserTemplate(Long templateId) {
        if (templateId == null || templateId <= 0) {
            return false;
        }
        GroupRuleDOExample example = new GroupRuleDOExample();
        example.createCriteria().andIsDeleteEqualTo(0).andTemplateIdEqualTo(templateId);

        long num = groupRuleDao.countByExample(example);
        logger.info("num = {}", num);

        return num > 0;
    }

    /**
     * 查询并组装规则信息.
     *
     * @param groupId 规则id
     * @return ''
     */
    private RuleGroupDTO queryAndAssembleGroup(Long groupId) {

        GroupRuleBaseResDTO group = groupRuleDbService.queryGroupRuleBaseInfo(groupId);
        if (group == null) {
            logger.info("groupId:{} 未查询到规则组信息", groupId);
            return null;
        }
        List<RuleDTO> ruleList = gainRuleInfo(groupId);
        return RuleGroupDTO.builder().id(groupId).bizCode(group.getBizCode())
                .templateId(group.getTemplateId()).createUser(group.getCreateUser()).updateUser(group.getUpdateUser())
                .createTime(group.getCreateTime()).updateTime(group.getUpdateTime()).rules(ruleList).build();
    }

    /**
     * 获取规则组规则列表.
     *
     * @param groupId 规则组id
     * @return ''
     */
    private List<RuleDTO> gainRuleInfo(Long groupId) {
        List<RuleBaseDTO> ruleBaseList = ruleDbService.queryRulesByGroupId(groupId);
        if (CollectionUtils.isEmpty(ruleBaseList)) {
            return Lists.newArrayList();
        }
        List<Long> ruleIds = ruleBaseList.stream().map(RuleBaseDTO::getId).collect(Collectors.toList());
        Map<Long, List<RuleMetaBaseDTO>> ruleMetaMap = getRuleMetaMap(ruleIds);
        Map<Long, String> ruleResultMap = getRuleResultMap(ruleIds);
        List<RuleDTO> ruleList = Lists.newArrayList();
        for (RuleBaseDTO rule : ruleBaseList) {
            List<RuleMetaBaseDTO> ruleMetaBaseList = ruleMetaMap.get(rule.getId());
            List<RuleMetaDTO> metas =
                    ruleMetaBaseList.stream().map(this::ruleMetaBaseToDTO).collect(Collectors.toList());
            RuleDTO ruleDTO = RuleDTO.builder().id(rule.getId()).result(ruleResultMap.get(rule.getId()))
                    .priority(rule.getPriority()).name(rule.getRuleName()).metas(metas).build();
            ruleList.add(ruleDTO);
        }
        return ruleList;
    }

    private Map<Long, String> getRuleResultMap(List<Long> ruleIds) {
        List<RuleResultBaseDTO> ruleResultBaseDTOS = ruleResultDbService.queryRuleResultByRuleIds(ruleIds);
        if (CollectionUtils.isEmpty(ruleResultBaseDTOS)) {
            return Maps.newHashMap();
        }
        return ruleResultBaseDTOS.stream().collect(
                Collectors.toMap(RuleResultBaseDTO::getRuleId, RuleResultBaseDTO::getResult));
    }

    private RuleMetaDTO ruleMetaBaseToDTO(RuleMetaBaseDTO ruleMetaBaseDTO) {
        return RuleMetaDTO.builder().id(ruleMetaBaseDTO.getId())
                .code(ruleMetaBaseDTO.getMetaCode()).value(ruleMetaBaseDTO.getMetaValue()).build();
    }

    private Map<Long, List<RuleMetaBaseDTO>> getRuleMetaMap(List<Long> ruleIds) {
        List<RuleMetaBaseDTO> ruleMetaList = ruleMetaDbService.queryRuleMetasByRuleIds(ruleIds);
        if (CollectionUtils.isEmpty(ruleMetaList)) {
            return Maps.newHashMap();
        }
        return ruleMetaList.stream().collect(Collectors.groupingBy(RuleMetaBaseDTO::getRuleId));
    }


    /**
     * 删除规则组参数验证.
     *
     * @param group ''
     */
    private void checkDelParam(DelGroupRuleDTO group) {
        if (group.getGroupId() == null) {
            throw new TransException(ResultEnum.PARAM_ERROR, "delGroup groupId不能为空");
        }
        if (StringUtils.isBlank(group.getOperator())) {
            throw new TransException(ResultEnum.PARAM_ERROR, "delGroup 操作人不能为空");
        }
    }

    /**
     * 验证查询入参.
     *
     * @param id id
     */
    private void checkQueryParam(Long id) {
        if (id == null) {
            throw new TransException(ResultEnum.PARAM_ERROR, "groupId不能为空");
        }
    }

    /**
     * 验证参数.
     *
     * @param req 入参
     * @return ''
     */
    private SaveGroupCheckResBO checkParam(RuleGroupDTO req) {
        if (req.getId() == null) {
            return addCheck(req);
        }
        return updateCheck(req);
    }

    /**
     * 新增规则组校验.
     *
     * @param req ''
     */
    private SaveGroupCheckResBO addCheck(RuleGroupDTO req) {
        //基本数据验证
        checkBaseValue(req);
        //数据业务验证
        return checkBizAddValue(req);
    }

    /**
     * 更新规则组入参校验.
     *
     * @param req ''
     */
    private SaveGroupCheckResBO updateCheck(RuleGroupDTO req) {
        checkUpdateBase(req);
        return checkBizUpdateValue(req);
    }


    /**
     * 更新校验基本入参数据.
     *
     * @param req ''
     */
    private void checkUpdateBase(RuleGroupDTO req) {
        if (StringUtils.isBlank(req.getUpdateUser())) {
            throw new TransException(ResultEnum.PARAM_ERROR, "更新group updateUser 不能为空");
        }
        if (req.getUpdateUser().length() >= MAX_USER_SIZE) {
            throw new TransException(ResultEnum.PARAM_ERROR, "更新group updateUser 长度过长");
        }
        checkRules(req);
    }

    /**
     * 更新参数业务校验.
     *
     * @param req ''
     * @return ''
     */
    private SaveGroupCheckResBO checkBizUpdateValue(RuleGroupDTO req) {
        //验证group是否存在
        GroupRuleBaseResDTO group = checkGroupExist(req.getId());
        //验证规则元数据
        checkMetaBiz(group.getTemplateId(), req.getRules());
        return null;
    }

    /**
     * 检测入参基本信息.
     *
     * @param req ''
     */
    private void checkBaseValue(RuleGroupDTO req) {
        if (StringUtils.isBlank(req.getName())) {
            throw new TransException(ResultEnum.PARAM_ERROR, "新增group name 不能为空");
        }
        if (req.getName().length() > MAX_NAME_SIZE) {
            throw new TransException(ResultEnum.PARAM_ERROR, "新增group name 长度超限");
        }
        if (StringUtils.isBlank(req.getCreateUser())) {
            throw new TransException(ResultEnum.PARAM_ERROR, "新增group createUser 不能为空");
        }
        if (req.getCreateUser().length() >= MAX_USER_SIZE) {
            throw new TransException(ResultEnum.PARAM_ERROR, "新增group createUser 长度过长");
        }
        if (StringUtils.isBlank(req.getBizCode())) {
            throw new TransException(ResultEnum.PARAM_ERROR, "新增group bizCode 不能为空");
        }
        if (req.getTemplateId() == null) {
            throw new TransException(ResultEnum.PARAM_ERROR, "新增group templateId 不能为空");
        }
        checkRules(req);
    }

    /**
     * 入参业务校验.
     *
     * @param req ''
     */
    private SaveGroupCheckResBO checkBizAddValue(RuleGroupDTO req) {
        //验证bizCode
        BusinessBaseResDTO business = businessDbService.queryBusinessByCode(req.getBizCode());
        if (business == null) {
            throw new TransException(ResultEnum.PARAM_ERROR, "新增group 对应业务线不存在");
        }

        SaveGroupCheckResBO saveGroupRuleBO = new SaveGroupCheckResBO();
        //验证模板信息
        RuleTemplateBaseResDTO ruleTemplateBaseResDTO =
                ruleTemplateDbService.queryRuleTemplateBaseInfoById(req.getTemplateId());
        if (ruleTemplateBaseResDTO == null) {
            throw new TransException(ResultEnum.PARAM_ERROR, "新增group 对应规则模板不存在");
        }
        saveGroupRuleBO.setScript(ruleTemplateBaseResDTO.getScript());
        //验证规则
        checkMetaBiz(req.getTemplateId(), req.getRules());
        return saveGroupRuleBO;
    }


    /**
     * 校验规则列表.
     *
     * @param req ''
     */
    private void checkRules(RuleGroupDTO req) {
        if (CollectionUtils.isEmpty(req.getRules())) {
            return;
        }
        if (req.getRules().size() > ruleCommonConfig.getSaveRuleNumLimit()) {
            throw new TransException(ResultEnum.PARAM_ERROR,
                    "保存rule个数不能超过" + ruleCommonConfig.getSaveRuleNumLimit());
        }
        for (RuleDTO ruleDTO : req.getRules()) {
            if (ruleDTO == null) {
                continue;
            }
            checkSaveRule(ruleDTO, req.getTemplateId());
        }
    }

    /**
     * 校验规则新增入参.
     *
     * @param rule       ''
     * @param templateId 模板id
     */
    private void checkSaveRule(RuleDTO rule, Long templateId) {
        if (StringUtils.isBlank(rule.getName())) {
            throw new TransException(ResultEnum.PARAM_ERROR, "保存rule name 不能为空");
        }
        if (rule.getName().length() > MAX_NAME_SIZE) {
            throw new TransException(ResultEnum.PARAM_ERROR, "保存rule name 长度超限");
        }
        if (rule.getPriority() == null) {
            throw new TransException(ResultEnum.PARAM_ERROR, "保存rule priority 不能为空");
        }
        if (rule.getPriority() > MAX_PRIORITY) {
            throw new TransException(ResultEnum.PARAM_ERROR, "保存rule priority 值不能大于" + MAX_PRIORITY);
        }
        if (StringUtils.isBlank(rule.getResult())) {
            throw new TransException(ResultEnum.PARAM_ERROR, "保存rule result 不能为空");
        }
        if (rule.getResult().length() >= MAX_RESULT_SIZE) {
            throw new TransException(ResultEnum.PARAM_ERROR, "保存rule result 长度超限");
        }
        if (CollectionUtils.isEmpty(rule.getMetas())) {
            throw new TransException(ResultEnum.PARAM_ERROR, "保存rule metas 不能为空");
        }

        for (RuleMetaDTO ruleMetaDTO : rule.getMetas()) {
            if (ruleMetaDTO == null) {
                continue;
            }
            checkRuleMeta(ruleMetaDTO);
        }
    }

    /**
     * 校验规则元数据入参.
     *
     * @param ruleMeta ''
     */
    private void checkRuleMeta(RuleMetaDTO ruleMeta) {
        if (StringUtils.isBlank(ruleMeta.getCode())) {
            throw new TransException(ResultEnum.PARAM_ERROR, "保存ruleMeta code 不能为空");
        }
        if (StringUtils.isBlank(ruleMeta.getValue())) {
            throw new TransException(ResultEnum.PARAM_ERROR, ruleMeta.getCode() + ": 保存ruleMeta value 不能为空");
        }
        if (ruleMeta.getValue().length() >= MAX_FIELD_SIZE) {
            throw new TransException(ResultEnum.PARAM_ERROR, ruleMeta.getCode() + ": 保存ruleMeta value长度超限");
        }
    }

    /**
     * 元数据业务校验.
     *
     * @param templateId 模板id
     * @param rules      规则列表.
     */
    private void checkMetaBiz(Long templateId, List<RuleDTO> rules) {
        //获取模板对应元数据code
        List<TemplateMetaBaseResDTO> templateMetas = templateMetaDbService.getTemplateMetaByTid(templateId);
        if (CollectionUtils.isEmpty(templateMetas)) {
            throw new TransException(ResultEnum.PARAM_ERROR, "保存rule 模板对应元数据不能为空");
        }
        List<String> metaCodes = templateMetas.stream().map(TemplateMetaBaseResDTO::getMetaCode)
                .collect(Collectors.toList());
        //验证规则请求元数据完整性
        checkRuleListMetaIntegrity(metaCodes, rules);
        //获取所有meta配置信息
        List<MetaBaseDTO> metaBaseList = metaDbService.queryMetaByCodes(metaCodes);
        if (CollectionUtils.isEmpty(metaBaseList)) {
            throw new TransException(ResultEnum.PARAM_ERROR, "保存ruleMeta 模板元数据基本信息不能为空");
        }
        Map<String, MetaBaseDTO> metaMap = metaBaseList.stream()
                .collect(Collectors.toMap(MetaBaseDTO::getMetaCode, Function.identity(), (key1, key2) -> key1));
        Map<String, List<String>> metaValuesMap = getMetaCodeValuesMap(rules);
        //验证规则请求元数据值类型及数量正确
        checkRuleListMetaValueType(metaCodes, metaMap, metaValuesMap);
        //验证规则请求元数据值数量正确
        checkRuleListMetaValueCount(metaCodes, templateMetas, metaMap, metaValuesMap);
    }

    /**
     * 验证规则请求元数据值数量正确.
     *
     * @param metaCodes     ''
     * @param templateMetas ''
     * @param metaMap       ""
     * @param metaValuesMap ""
     */
    private void checkRuleListMetaValueCount(List<String> metaCodes,
                                             List<TemplateMetaBaseResDTO> templateMetas,
                                             Map<String, MetaBaseDTO> metaMap,
                                             Map<String, List<String>> metaValuesMap) {
        Map<String, TemplateMetaBaseResDTO> templateMetaMap = templateMetas.stream()
                .collect(Collectors.toMap(TemplateMetaBaseResDTO::getMetaCode,
                        Function.identity(), (key1, key2) -> key1));
        for (String metaCode : metaCodes) {
            TemplateMetaBaseResDTO templateMeta = templateMetaMap.get(metaCode);
            MetaBaseDTO metaBaseDTO = metaMap.get(metaCode);
            if (metaBaseDTO == null) {
                throw new RuntimeException(metaCode + ":未查询到基本配置信息");
            }
            //完整性已判断无需非空判断
            OperatorEnum operator = OperatorEnum.getOperatorEnumByCode(templateMeta.getOperator());
            if (operator == null) {
                throw new RuntimeException("template:" + templateMeta.getId() + " " + metaCode + " 对应模板元数据配置方法不存在");
            }
            if (operator == OperatorEnum.BETWEEN) {
                checkBetweenMetaValue(metaValuesMap.get(metaCode), metaCode, metaBaseDTO.getDefaultValue());
            }
        }
    }

    /**
     * 验证between参数.
     *
     * @param metaValues   ''
     * @param metaCode     ''
     * @param defaultValue ''
     */
    private void checkBetweenMetaValue(List<String> metaValues, String metaCode, String defaultValue) {
        for (String value : metaValues) {
            if (defaultValue.equals(value)) {
                continue;
            }
            String[] values = value.split(RuleConstant.SPLIT);
            if (values.length != PARAM_SIZE) {
                throw new TransException(ResultEnum.PARAM_ERROR, metaCode + " configValue size must be " + PARAM_SIZE);
            }
            //验证第一个数小于第二个数
            double firstNum = NumberUtils.toDouble(values[0]);
            double secNum = NumberUtils.toDouble(values[1]);
            if (secNum <= firstNum) {
                throw new TransException(ResultEnum.PARAM_ERROR, metaCode + ": configValue 第一个数值需要小于第二个值 ");
            }
        }
    }

    /**
     * 验证所有规则请求元数据值类型正确
     *
     * @param metaCodes     元数据code
     * @param metaMap       ""
     * @param metaValuesMap ""
     */
    private void checkRuleListMetaValueType(List<String> metaCodes,
                                            Map<String, MetaBaseDTO> metaMap,
                                            Map<String, List<String>> metaValuesMap) {

        for (String metaCode : metaCodes) {
            MetaBaseDTO metaBaseDTO = metaMap.get(metaCode);
            if (metaBaseDTO == null) {
                throw new RuntimeException(metaCode + ":未查询到基本配置信息");
            }
            MetaValueTypeEnum valueType = MetaValueTypeEnum.getValueTypeByCode(metaBaseDTO.getValueType());
            if (valueType == null) {
                throw new RuntimeException(metaBaseDTO.getMetaCode() + " 对应元数据配置数据类型不存在");
            }
            if (valueType == MetaValueTypeEnum.NUMBER) {
                checkNumberValue(metaValuesMap.get(metaCode), metaCode, metaBaseDTO.getDefaultValue());
            }
        }
    }

    /**
     * 校验数值类型数据.
     *
     * @param metaValues   ''
     * @param metaCode     ''
     * @param defaultValue ''
     */
    private void checkNumberValue(List<String> metaValues, String metaCode, String defaultValue) {
        for (String value : metaValues) {
            if (defaultValue.equals(value)) {
                continue;
            }
            String[] values = value.split(RuleConstant.SPLIT);
            for (String str : values) {
                if (!NumberUtils.isNumber(str)) {
                    throw new TransException(ResultEnum.PARAM_ERROR, "保存ruleMeta metaCode:" + metaCode + "元数据值类型错误");
                }
            }
        }
    }

    /**
     * 获取元数据值列表.
     *
     * @param rules ''
     * @return ''
     */
    private Map<String, List<String>> getMetaCodeValuesMap(List<RuleDTO> rules) {
        List<RuleMetaDTO> allMetas = Lists.newArrayList();
        for (RuleDTO rule : rules) {
            allMetas.addAll(rule.getMetas());
        }
        return allMetas.stream().collect(Collectors.groupingBy(RuleMetaDTO::getCode,
                Collectors.mapping(RuleMetaDTO::getValue, Collectors.toList())));
    }

    /**
     * 验证所有规则请求元数据完整性
     *
     * @param metaCodes ""
     * @param rules     ''
     */
    private void checkRuleListMetaIntegrity(List<String> metaCodes, List<RuleDTO> rules) {
        if (CollectionUtils.isEmpty(rules)) {
            return;
        }

        for (RuleDTO rule : rules) {
            if (rule == null) {
                continue;
            }
            checkRuleMetaIntegrity(rule.getMetas(), metaCodes);
        }
    }

    /**
     * 验证规则元数据完整性.
     *
     * @param metas     请求元数据
     * @param metaCodes 元数据code
     */
    private void checkRuleMetaIntegrity(List<RuleMetaDTO> metas, List<String> metaCodes) {
        List<String> reqMetaCods = metas.stream().map(RuleMetaDTO::getCode).collect(Collectors.toList());
        //验证元数据code
        if (!reqMetaCods.containsAll(metaCodes)) {
            metaCodes.removeAll(reqMetaCods);
            throw new TransException(ResultEnum.PARAM_ERROR, "保存rule metaCode:" + metaCodes + "不能缺失");
        }
        if (metaCodes.size() != reqMetaCods.size()) {
            Set<String> reqSet = Sets.newHashSet(reqMetaCods);
            if (reqMetaCods.size() != reqSet.size()) {
                //获得list与set的差集
                Collection collection = org.apache.commons.collections4.CollectionUtils
                    .disjunction(reqMetaCods, reqSet);
                throw new TransException(ResultEnum.PARAM_ERROR, "保存rule 请求元数据code:" + collection + " 重复");
            }
            reqMetaCods.removeAll(metaCodes);
            throw new TransException(ResultEnum.PARAM_ERROR, "保存rule 请求元数据code:" + reqMetaCods + "不是规则需要code");
        }
    }

    /**
     * 验证group存在.
     *
     * @param groupId ''
     */
    private GroupRuleBaseResDTO checkGroupExist(Long groupId) {
        GroupRuleBaseResDTO groupRuleBaseResDTO = groupRuleDbService.queryGroupRuleBaseInfo(groupId);
        if (groupRuleBaseResDTO == null) {
            throw new TransException(ResultEnum.PARAM_ERROR, "更新group groupId:" + groupId + " 规则组不存在");
        }
        return groupRuleBaseResDTO;
    }

    /**
     * 组装新增规则组对象.
     *
     * @param req             ''
     * @param saveGroupRuleBO 规则.
     * @return ''
     */
    private AddRuleGroupReqDTO gainAddRuleGroup(RuleGroupDTO req, SaveGroupCheckResBO saveGroupRuleBO) {
        AddRuleGroupReqDTO addRuleGroupReqDTO = AddRuleGroupReqDTO.builder().name(req.getName())
                .createUser(req.getCreateUser()).bizCode(req.getBizCode())
                .templateId(req.getTemplateId()).script(saveGroupRuleBO.getScript()).build();
        List<RuleReqDTO> ruleList = gainRuleReqDTO(req.getRules(), req.getCreateUser());
        addRuleGroupReqDTO.setRules(ruleList);
        return addRuleGroupReqDTO;
    }

    /**
     * 组装更新规则组对象.
     *
     * @param req ''
     * @return ''
     */
    private UpdateRuleGroupReqDTO gainUpdateRuleGroup(RuleGroupDTO req) {
        UpdateRuleGroupReqDTO updateRuleGroupReqDTO = UpdateRuleGroupReqDTO.builder()
                .groupId(req.getId()).operator(req.getUpdateUser()).build();
        List<RuleReqDTO> ruleReqDTOList = gainRuleReqDTO(req.getRules(), req.getUpdateUser());
        updateRuleGroupReqDTO.setRules(ruleReqDTOList);
        return updateRuleGroupReqDTO;
    }

    /**
     * 组装规则信息.
     *
     * @param rules    规则列表
     * @param operator 操作人.
     * @return ''
     */
    private List<RuleReqDTO> gainRuleReqDTO(List<RuleDTO> rules, String operator) {
        if (CollectionUtils.isEmpty(rules)) {
            return Lists.newArrayList();
        }
        List<RuleReqDTO> ruleReqDTOList = Lists.newArrayList();
        for (RuleDTO ruleDTO : rules) {
            if (ruleDTO == null) {
                continue;
            }
            List<RuleMetaReqDTO> ruleMetas = getRuleMetas(ruleDTO.getMetas());
            RuleReqDTO ruleReqDTO = RuleReqDTO.builder().name(ruleDTO.getName())
                    .priority(ruleDTO.getPriority()).result(ruleDTO.getResult())
                    .operator(operator).metas(ruleMetas).build();
            ruleReqDTOList.add(ruleReqDTO);
        }
        return ruleReqDTOList;
    }

    /**
     * 组装规则元数据信息.
     *
     * @param metas 元数据
     * @return ''
     */
    private List<RuleMetaReqDTO> getRuleMetas(List<RuleMetaDTO> metas) {
        if (CollectionUtils.isEmpty(metas)) {
            return Lists.newArrayList();
        }
        return metas.stream().map(ruleMetaDTO -> BeanConvertUtil.conver(ruleMetaDTO, RuleMetaReqDTO.class))
                .collect(Collectors.toList());
    }

}

