package com.ark.rule.platform.domain.dto.inner;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.io.Serializable;

/**
 * 规则配置信息.
 *
 */
@Data
@Builder
public class IRuleDTO implements Serializable {

    private static final long serialVersionUID = 2642339345521449572L;
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

    @Tolerate
    public IRuleDTO() {
    }
}
