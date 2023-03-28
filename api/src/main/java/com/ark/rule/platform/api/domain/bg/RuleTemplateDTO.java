package com.ark.rule.platform.api.domain.bg;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 规则模板实体.
 *
 */
@Data
public class RuleTemplateDTO implements Serializable {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 业务线编码
     */
    private String bizCode;
    /**
     * 业务线名称
     */
    private String bizName;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 规则脚本
     */
    private String script;
    /**
     * 元数据列表
     */
    private List<RuleTemplateMetaDTO> metaList;

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