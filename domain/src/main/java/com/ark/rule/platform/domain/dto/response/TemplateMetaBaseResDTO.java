
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
public class TemplateMetaBaseResDTO implements Serializable {
    private static final long serialVersionUID = -1321344844082550485L;

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

