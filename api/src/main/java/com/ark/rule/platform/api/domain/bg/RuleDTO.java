package com.ark.rule.platform.api.domain.bg;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.io.Serializable;
import java.util.List;

/**
 * 规则.
 *
 */
@Data
@Builder
public class RuleDTO implements Serializable {

    /**
     * 规则id
     */
    private Long id;

    /**
     * 规则名称
     */
    private String name;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 返回结果
     */
    private String result;

    /**
     * 元数据列表
     */
    private List<RuleMetaDTO> metas;

    @Tolerate
    public RuleDTO() {
    }
}
