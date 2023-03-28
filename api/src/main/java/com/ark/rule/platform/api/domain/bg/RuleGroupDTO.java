
package com.ark.rule.platform.api.domain.bg;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 规则和规则组保存请求体.
 *
 */
@Data
@Builder
public class RuleGroupDTO implements Serializable {

    /**
     * 规则组id
     */
    private Long id;

    /**
     * 规则组名称
     */
    private String name;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 修改人
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

    /**
     * 业务线编码
     */
    private String bizCode;

    /**
     * 规则编排模板id
     */
    private Long templateId;

    /**
     * 规则列表
     */
    private List<RuleDTO> rules;

    @Tolerate
    public RuleGroupDTO() {
    }

}
