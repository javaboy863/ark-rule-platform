package com.ark.rule.platform.domain.service.bo;

import com.alibaba.fastjson.JSON;
import com.ark.rule.platform.common.aviator.AviatorUtil;
import com.ark.rule.platform.common.aviator.bo.ExecuteArgBO;
import com.ark.rule.platform.common.constant.RuleConstant;
import com.ark.rule.platform.common.enums.OperatorEnum;
import com.ark.rule.platform.domain.dto.inner.IGroupRuleDTO;
import com.ark.rule.platform.domain.dto.inner.IRuleDTO;
import com.ark.rule.platform.domain.dto.inner.IRuleMetaDTO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

/**
 * 规则执行器实体.
 *
 */
@Data
@Builder
public class ExecutorEntity {
    private static final Logger logger = LoggerFactory.getLogger(ExecutorEntity.class);

    /**
     * 规则组id
     */
    private Long groupId;

    /**
     * 执行脚本
     */
    private String script;

    /**
     * 模板id
     */
    private Long templateId;

    /**
     * 规则列表.
     */
    private List<IRuleDTO> ruleList;

    /**
     * 规则元数据信息 key:ruleId.
     */
    private Map<Long, List<IRuleMetaDTO>> ruleMetaMap;
    /**
     * 元数据规则计算实体.
     */
    private Map<String, MetaExecuteBO> metaExecuteBOMap;
    /**
     * 剪枝算法开关.
     */
    private Boolean alphaBetaFlag;


    @Tolerate
    public ExecutorEntity() {
    }

    /**
     * c端规则计算实体.
     *
     * @param group           规则组信息
     * @param ruleList        规则列表.
     * @param ruleMetas       规则组元数据信息.
     * @param metaExecuteList 规则组元数据计算对象列表
     * @param alphaBetaFlag   剪枝算法开关
     * @return ''
     */
    public static ExecutorEntity newExecuteInstance(IGroupRuleDTO group,
                                                    List<IRuleDTO> ruleList, List<IRuleMetaDTO> ruleMetas,
                                                    List<MetaExecuteBO> metaExecuteList, Boolean alphaBetaFlag) {
        if (group == null || CollectionUtils.isEmpty(ruleMetas) || CollectionUtils.isEmpty(metaExecuteList)) {
            throw new IllegalArgumentException("execute instance param miss");
        }
        ExecutorEntity entity = ExecutorEntity.builder().groupId(group.getId())
                .script(group.getScript()).ruleList(ruleList).alphaBetaFlag(alphaBetaFlag).build();

        Map<Long, List<IRuleMetaDTO>> ruleMetaMap =
                ruleMetas.stream().collect(Collectors.groupingBy(IRuleMetaDTO::getRuleId));
        entity.setRuleMetaMap(ruleMetaMap);

        Map<String, MetaExecuteBO> metaExecuteBOMap = metaExecuteList.stream()
                .collect(Collectors.toMap(MetaExecuteBO::getMetaCode, Function.identity(), (key1, key2) -> key1));
        entity.setMetaExecuteBOMap(metaExecuteBOMap);
        return entity;
    }

    /**
     * 执行规则.
     *
     * @return 规则结果.
     */
    public Long execute() {
        List<IRuleDTO> filterRules = alphaBetaFilterRule();
        if (CollectionUtils.isEmpty(filterRules)) {
            return null;
        }
        //规则优先排序
        List<IRuleDTO> sortRules = sortRule(filterRules);
        if (CollectionUtils.isEmpty(sortRules)) {
            return null;
        }
        for (IRuleDTO rule : sortRules) {
            if (rule == null) {
                continue;
            }
            Map<String, Object> executeParam = getExecuteParam(rule);
            if (MapUtils.isEmpty(executeParam)) {
                logger.info("rule:{}, executeParam is empty", rule);
                continue;
            }
            boolean result = AviatorUtil.execute(script, executeParam);
            if (result) {
                return rule.getId();
            }
        }
        return null;
    }

    /**
     * 剪枝算法过滤rule.
     * todo 注: 一期剪枝算法仅支持全部&&的表达式.
     * 若后期需要支持一层的|| 可以用 && 取交集  || 取并集实现.
     * 多层级表达式例:(&&)||(||)需要另想办法进行实现.
     *
     * @return ''
     */
    private List<IRuleDTO> alphaBetaFilterRule() {
        if (BooleanUtils.isNotTrue(alphaBetaFlag)) {
            return ruleList;
        }
        Set<Long> ruleIdSet = Sets.newHashSet();
        for (Map.Entry<String, MetaExecuteBO> entry : metaExecuteBOMap.entrySet()) {
            //元数据值
            MetaExecuteBO metaExecute = entry.getValue();
            //该元数据可能命中的规则列表
            List<Long> possibleIds = getPossibleRule(metaExecute);
            if (CollectionUtils.isEmpty(possibleIds)) {
                logger.debug("metaCode:{} 未命中任何规则", metaExecute.getMetaCode());
                return Lists.newArrayList();
            }
            if (ruleIdSet.size() == 0) {
                logger.debug("first metaCode:{} 可能命中的规则ids:{}", entry.getKey(), possibleIds);
                ruleIdSet.addAll(possibleIds);
            } else {
                //取交集
                ruleIdSet.retainAll(possibleIds);
            }
            if (CollectionUtils.isEmpty(ruleIdSet)) {
                return Lists.newArrayList();
            }
        }
        logger.info("剪枝算法后剩余规则列表:{}", ruleIdSet);
        //过滤不合格规则
        return ruleList.stream().filter(rule -> ruleIdSet.contains(rule.getId())).collect(Collectors.toList());
    }

    /**
     * 获取该元数据可能的命中的规则列表.
     *
     * @param metaExecute 规则计算元数据数据
     * @return ''
     */
    private List<Long> getPossibleRule(MetaExecuteBO metaExecute) {
        //获取规则元数据操作符
        String function = OperatorEnum.getFunctionByOperator(metaExecute.getOperator());
        if (StringUtils.isBlank(function)) {
            throw new RuntimeException(metaExecute.getOperator() + "操作无function");
        }
        Map<String, List<Long>> metaValueRuleMap = metaExecute.getMetaValueRuleMap();
        Map<String, Object> executeParam = Maps.newHashMap();
        //该元数据请求值可能命中的规则列表.
        List<Long> possibleRuleId = Lists.newArrayList();
        for (Map.Entry<String, List<Long>> entry : metaValueRuleMap.entrySet()) {
            String configValue = entry.getKey();
            ExecuteArgBO executeArg = ExecuteArgBO.builder().reqArg(metaExecute.getReqValue())
                    .configArg(configValue).defaultArg(metaExecute.getDefaultValue()).build();
            executeParam.put(RuleConstant.FUNCTION_PARAM_NAME, executeArg);
            //单元数据判断是否命中
            boolean result = AviatorUtil.execute(function, executeParam);
            if (!result || CollectionUtils.isEmpty(entry.getValue())) {
                logger.debug("metaCode:{}, function:{}, reqValue:{}, configValue:{}, defaultValue:{} 未匹配, 过滤rule:{}",
                        metaExecute.getMetaCode(), metaExecute.getOperator(), metaExecute.getReqValue(),
                        configValue, metaExecute.getDefaultValue(), entry.getValue());
                continue;
            }
            possibleRuleId.addAll(entry.getValue());
        }
        return possibleRuleId;
    }

    /**
     * 规则优先级排序.
     * 1. 按规则优先级倒序, 值越大越前
     * 2. 按规则id倒序, 值越大越前.
     *
     * @param filterRules 规则列表
     * @return ''
     */
    private List<IRuleDTO> sortRule(List<IRuleDTO> filterRules) {
        if (CollectionUtils.isEmpty(filterRules)) {
            return Lists.newArrayList();
        }
        return filterRules.stream().sorted((rule1, rule2) -> {
            int flag = rule2.getPriority() - rule1.getPriority();
            if (flag == 0) {
                flag = (int) (rule2.getId() - rule1.getId());
            }
            return flag;
        }).collect(Collectors.toList());
    }

    /**
     * 组装计算参数.
     * 注:为防止以后模板可变,而规则组不变, 这里使用规则的元数据进行参数组装,
     * 在b端进行了规则的元数据和模板元数据完全一致校验.
     *
     * @param rule 规则信息
     * @return ''
     */
    private Map<String, Object> getExecuteParam(IRuleDTO rule) {
        List<IRuleMetaDTO> ruleMetas = ruleMetaMap.get(rule.getId());
        Map<String, Object> param = Maps.newHashMap();
        for (IRuleMetaDTO ruleMeta : ruleMetas) {
            if (ruleMeta == null || StringUtils.isBlank(ruleMeta.getMetaValue())) {
                logger.info("rule meta config error:{}", JSON.toJSONString(ruleMeta));
                throw new RuntimeException("ruleMeta配置有误:" + JSON.toJSONString(ruleMeta));
            }
            String metaCode = ruleMeta.getMetaCode();
            MetaExecuteBO metaExecuteBO = metaExecuteBOMap.get(metaCode);
            if (metaExecuteBO == null) {
                logger.info("cannot find metaExecute code:{}", metaCode);
                throw new RuntimeException("metaCode:" + metaCode + "未获取规则计算信息");
            }
            ExecuteArgBO executeArg = ExecuteArgBO.builder()
                    .reqArg(metaExecuteBO.getReqValue())
                    .defaultArg(metaExecuteBO.getDefaultValue())
                    .configArg(ruleMeta.getMetaValue()).build();
            param.put(metaCode, executeArg);
        }
        return param;
    }
}
