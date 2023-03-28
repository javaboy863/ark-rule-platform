
package com.ark.rule.platform.domain.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 新增规则请求体.
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleReqDTO implements Serializable {
    private static final long serialVersionUID = 5077124245198844941L;
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
     * 操作人.
     */
    private String operator;

    /**
     * 规则元数据列表.
     */
    private List<RuleMetaReqDTO> metas;
}

