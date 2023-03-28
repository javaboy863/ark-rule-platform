
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
public class AddRuleGroupReqDTO implements Serializable {
    /**
     * 规则组名称
     */
    private String name;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 业务线编码
     */
    private String bizCode;

    /**
     * 规则编排模板id
     */
    private Long templateId;
    /**
     * 规则脚本
     */
    private String script;

    /**
     * 规则配置列表
     */
    private List<RuleReqDTO> rules;
}

