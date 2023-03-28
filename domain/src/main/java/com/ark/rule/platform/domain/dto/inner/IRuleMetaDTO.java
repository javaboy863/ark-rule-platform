package com.ark.rule.platform.domain.dto.inner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 规则元数据配置信息.
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IRuleMetaDTO implements Serializable {
    private static final long serialVersionUID = -1760016861833181469L;
    private Long id;

    /**
     * 规则id
     */
    private Long ruleId;

    /**
     * 元数据编码
     */
    private String metaCode;

    /**
     * 值
     */
    private String metaValue;
}

