package com.ark.rule.platform.domain.service.guava;

import com.ark.rule.platform.domain.dto.inner.IRuleResultDTO;

/**
 * 描述类的功能.
 *
 */
public interface IRuleResultGuavaService {

    /**
     * 获取规则返回信息.
     *
     * @param ruleId 规则id
     * @return ''
     */
    IRuleResultDTO getResultByRuleId(Long ruleId);
}
