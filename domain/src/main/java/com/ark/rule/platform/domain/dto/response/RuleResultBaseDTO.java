
package com.ark.rule.platform.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 描述类的功能.
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleResultBaseDTO implements Serializable {
    private static final long serialVersionUID = 4728083921480490928L;
    private Long id;

    /**
     * 规则id
     */
    private Long ruleId;

    /**
     * 结果
     */
    private String result;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 更新人
     */
    private String updateUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}

