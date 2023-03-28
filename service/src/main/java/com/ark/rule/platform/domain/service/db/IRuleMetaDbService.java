
package com.ark.rule.platform.domain.service.db;


import com.ark.rule.platform.domain.dto.response.RuleMetaBaseDTO;
import java.util.List;

/**
 * 查询db规则元数据信息.
 *
 */
public interface IRuleMetaDbService {
    /**
     * 批量查询规则组的所有元数据配置.
     *
     * @param ruleIds ''
     * @return ''
     */
    List<RuleMetaBaseDTO> queryRuleMetasByRuleIds(List<Long> ruleIds);
}

