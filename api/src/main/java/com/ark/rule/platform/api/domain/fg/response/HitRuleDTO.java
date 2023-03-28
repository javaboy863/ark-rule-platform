package com.ark.rule.platform.api.domain.fg.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 命中规则信息.
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HitRuleDTO implements Serializable {
    /**
     * 命中规则id.
     */
    private Long hitRuleId;
    /**
     * 执行结果
     */
    private String result;
}

