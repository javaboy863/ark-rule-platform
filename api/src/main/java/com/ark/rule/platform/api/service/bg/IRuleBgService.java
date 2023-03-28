

package com.ark.rule.platform.api.service.bg;

import com.ark.rule.platform.api.Result;
import com.ark.rule.platform.api.domain.bg.DelGroupRuleDTO;
import com.ark.rule.platform.api.domain.bg.RuleGroupDTO;

/**
 * 规则B端增删改查服务.
 *
 */
public interface IRuleBgService {

    /**
     * 保存规则(组).
     * RuleGroupDTO.id不存在,则为新增
     * RuleGroupDTO.id存在,则为保存
     * 修改操作分解为:先删除,再插入
     *
     * @param req 请求参数
     * @return 返回groupId
     */
    Result<Long> saveRuleGroup(RuleGroupDTO req);

    /**
     * 根据规则组id删除规则组.
     *
     * @param group 规则组
     * @return 影响行数
     */
    Result deleteRuleGroupById(DelGroupRuleDTO group);

    /**
     * 根据id查询规则(组).
     * 仅查询有效的规则.
     *
     * @param id 规则组id
     * @return 规则组信息
     */
    Result<RuleGroupDTO> queryRuleGroupById(Long id);

    /**
     * 是否使用了某个模板.
     *
     * @param templateId 模板id
     * @return 是/否
     */
    boolean isUserTemplate(Long templateId);
}
