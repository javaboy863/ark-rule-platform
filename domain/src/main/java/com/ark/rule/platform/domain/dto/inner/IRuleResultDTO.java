
package com.ark.rule.platform.domain.dto.inner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 规则返回信息.
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IRuleResultDTO implements Serializable {
    private static final long serialVersionUID = 2689853001785601538L;
    private Long id;

    /**
     * 规则id
     */
    private Long ruleId;

    /**
     * 结果
     */
    private String result;
}

