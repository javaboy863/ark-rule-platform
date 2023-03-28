package com.ark.rule.platform.api.domain.bg;

import lombok.Data;

/**
 * 删除规则组.
 *
 */
@Data
public class DelGroupRuleDTO {
    /**
     * 规则组id.
     */
    private Long groupId;
    /**
     * 操作人
     */
    private String operator;
}

