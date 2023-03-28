
package com.ark.rule.platform.domain.service.db;


import com.ark.rule.platform.domain.dto.response.TemplateMetaBaseResDTO;
import java.util.List;

/**
 * 查询db模板元数据信息.
 *
 */
public interface ITemplateMetaDbService {
    /**
     * 根据模板id获取该模板元数据配置信息.
     *
     * @param templateId 模板id
     * @return ''
     */
    List<TemplateMetaBaseResDTO> getTemplateMetaByTid(Long templateId);
}

