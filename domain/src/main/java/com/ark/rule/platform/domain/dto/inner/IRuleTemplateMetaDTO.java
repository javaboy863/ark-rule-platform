
package com.ark.rule.platform.domain.dto.inner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 规则模板元数据对应.
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IRuleTemplateMetaDTO {
    private Long id;

    /**
     * 模板id
     */
    private Long templateId;

    /**
     * 元数据编码
     */
    private String metaCode;

    /**
     * 操作符
     */
    private String operator;
}

