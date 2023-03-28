
package com.ark.rule.platform.domain.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 规则组新增.
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRuleGroupReqDTO implements Serializable {
    private static final long serialVersionUID = 3435565736193945098L;
    /**
     * 规则组id.
     */
    private Long groupId;

    /**
     * 更新人
     */
    private String operator;

    /**
     * 规则配置列表
     */
    private List<RuleReqDTO> rules;
}

