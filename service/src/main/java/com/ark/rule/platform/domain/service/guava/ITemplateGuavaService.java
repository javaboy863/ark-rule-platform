
package com.ark.rule.platform.domain.service.guava;

import com.ark.rule.platform.domain.dao.domain.RuleTemplateDO;
import java.util.List;

/**
 * 规则模板guava服务.
 *
 */
public interface ITemplateGuavaService {
    /**
     * 查询规则组的规则模板.
     *
     * @param ids ''
     * @return ''
     */
    List<RuleTemplateDO> getTemplatesByIdList(List<Long> ids);
}
