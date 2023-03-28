package com.ark.rule.platform.api.domain.bg;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 规则模板对应的元数据.
 *
 */
@Data
public class RuleTemplateMetaDTO implements Serializable {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 模板id
     */
    private Long templateId;

    /**
     * 元数据编码
     */
    private String metaCode;

    /**
     * 操作符
     */
    private String operator;

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
