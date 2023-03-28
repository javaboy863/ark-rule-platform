
package com.ark.rule.platform.domain.dto.inner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 规则组配置信息.
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IGroupRuleDTO implements Serializable {
    private static final long serialVersionUID = -8677116805090349363L;
    private Long id;

    /**
     * 名称
     */
    private String groupName;

    /**
     * 业务线编码
     */
    private String bizCode;

    /**
     * 模板id
     */
    private Long templateId;

    /**
     * 规则脚本
     */
    private String script;

    /**
     * 0:未删除,1:已删除
     */
    private Integer isDelete;
}

