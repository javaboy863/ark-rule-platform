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
public class RuleBaseDTO implements Serializable {
    private static final long serialVersionUID = 7168596143552255154L;
    private Long id;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 优先级，倒序
     */
    private Integer priority;

    /**
     * 规则组id
     */
    private Long groupId;

    /**
     * 0:未删除,1:已删除
     */
    private Integer isDelete;

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

