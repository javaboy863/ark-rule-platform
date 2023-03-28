
package com.ark.rule.platform.domain.dto.inner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 规则模板信息.
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IRuleTemplateDTO implements Serializable {
    private static final long serialVersionUID = -4308884996055452493L;
    private Long id;

    /**
     * 业务线编码
     */
    private String bizCode;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 规则脚本
     */
    private String script;

    /**
     * 0:未删除,1:已删除
     */
    private Integer isDelete;
}

