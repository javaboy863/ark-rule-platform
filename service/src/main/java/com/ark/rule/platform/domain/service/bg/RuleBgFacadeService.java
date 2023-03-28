
package com.ark.rule.platform.domain.service.bg;


import com.ark.rule.platform.domain.dto.req.AddRuleGroupReqDTO;
import com.ark.rule.platform.domain.dto.req.DelGroupRuleReqDTO;
import com.ark.rule.platform.domain.dto.req.UpdateRuleGroupReqDTO;

/**
 * 规则facade层接口.
 *
 */
public interface RuleBgFacadeService {

    /**
     * 新增规则配置.
     *
     * @param ruleGroup 规则组配置.
     * @return 规则组id
     */
    Long addRuleGroup(AddRuleGroupReqDTO ruleGroup);

    /**
     * 更新规则组.
     *
     * @param groupRule ''
     */
    void updateRuleGroup(UpdateRuleGroupReqDTO groupRule);

    /**
     * 删除规则组.
     *
     * @param delGroupRuleReqDTO ''
     */
    void deleteGroupRule(DelGroupRuleReqDTO delGroupRuleReqDTO);
}

