
package com.ark.rule.platform.domain.service.db;


import com.ark.rule.platform.domain.dto.inner.IRuleTemplateDTO;
import com.ark.rule.platform.domain.dto.response.RuleTemplateBaseResDTO;
import java.util.List;

/**
 * 查询db规则模板信息.
 *
 */
public interface IRuleTemplateDbService {
    /**
     * 查询所有规则模板信息.
     *
     * @return ''
     */
    List<IRuleTemplateDTO> queryAllRuleTemplate();

    /**
     * 查询规则模板基本信息.
     *
     * @param templateId 模板id
     * @return ''
     */
    RuleTemplateBaseResDTO queryRuleTemplateBaseInfoById(Long templateId);
}

