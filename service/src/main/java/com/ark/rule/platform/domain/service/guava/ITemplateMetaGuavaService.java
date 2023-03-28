
package com.ark.rule.platform.domain.service.guava;

import com.ark.rule.platform.domain.dto.inner.IRuleTemplateMetaDTO;
import java.util.List;

/**
 * 规则模板元数据guava服务.
 *
 */
public interface ITemplateMetaGuavaService {
    /**
     * 根据模板id获取该模板元数据配置信息.
     *
     * @param templateId 模板id
     * @return ''
     */
    List<IRuleTemplateMetaDTO> getTemplateMetaByTid(Long templateId);
}

