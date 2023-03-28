
package com.ark.rule.platform.domain.service.db;


import com.ark.rule.platform.domain.dto.response.RuleBaseDTO;
import java.util.List;

/**
 * 查询db规则信息.
 *
 */
public interface IRuleDbService {
    /**
     * 根据规则组id查询有效规则.
     *
     * @param groupId 规则组id
     * @return ''
     */
    List<RuleBaseDTO> queryRulesByGroupId(Long groupId);
}

