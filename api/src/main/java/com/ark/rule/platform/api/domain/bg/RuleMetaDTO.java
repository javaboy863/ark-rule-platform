package com.ark.rule.platform.api.domain.bg;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

/**
 * 元数据.
 *
 */
@Data
@Builder
public class RuleMetaDTO implements Serializable {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 元数据编码
     */
    private String code;

    /**
     * 元数据值
     */
    private String value;

    @Tolerate
    public RuleMetaDTO() {
    }
}
