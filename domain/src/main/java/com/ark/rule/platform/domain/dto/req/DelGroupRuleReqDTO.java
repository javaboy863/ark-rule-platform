
package com.ark.rule.platform.domain.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 删除规则组请求.
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DelGroupRuleReqDTO {
    /**
     * 规则组id.
     */
    private Long groupId;
    /**
     * 操作人.
     */
    private String operator;
}

