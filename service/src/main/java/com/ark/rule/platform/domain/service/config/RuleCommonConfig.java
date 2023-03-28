package com.ark.rule.platform.domain.service.config;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * 规则conf配置. 来自配置中心
 *
 */
@Service
@Scope("singleton")
public class RuleCommonConfig {
    /**
     * 计算规则,剪枝算法开关 0；关 1：开
     */
    private int executorAlphaBetaFilter;
    /**
     * 保存规则组时单次最多添加的规则条数.
     */
    private int saveRuleNumLimit;
    /**
     * 批量规则计算的每次最大规则组的个数.
     */
    private int batchExecutorNumLimit;
    /**
     * 直接查询db开关 0；关 1：开.
     */
    private int bgQueryDbSwitch;

    public int getExecutorAlphaBetaFilter() {
        return executorAlphaBetaFilter;
    }

    public int getSaveRuleNumLimit() {
        return saveRuleNumLimit;
    }

    public int getBatchExecutorNumLimit() {
        return batchExecutorNumLimit;
    }

    public int getBgQueryDbSwitch() {
        return bgQueryDbSwitch;
    }
}

