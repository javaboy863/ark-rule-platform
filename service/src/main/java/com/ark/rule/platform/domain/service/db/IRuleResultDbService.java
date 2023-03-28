
package com.ark.rule.platform.domain.service.db;


import com.ark.rule.platform.domain.dto.response.RuleResultBaseDTO;
import java.util.List;

/**
 * 查询db规则返回信息.
 *
 */
public interface IRuleResultDbService {
    /**
     * 批量查询规则返回信息.
     *
     * @param ruleIds 规则ids
     * @return ''
     */
    List<RuleResultBaseDTO> queryRuleResultByRuleIds(List<Long> ruleIds);
}

