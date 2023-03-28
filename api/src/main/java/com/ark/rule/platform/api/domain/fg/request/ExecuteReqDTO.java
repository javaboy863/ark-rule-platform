
package com.ark.rule.platform.api.domain.fg.request;

import com.ark.rule.platform.api.domain.fg.RuleReqDTO;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.io.Serializable;
import java.util.List;

/**
 * 执行规则请求体..
 *
 */
@Data
@Builder
public class ExecuteReqDTO implements Serializable {
    /**
     * 规则组id
     */
    private Long groupId;
    /**
     * 业务线编码
     */
    private String bizCode;
    /**
     * 请求的元数据
     */
    private List<RuleReqDTO> metas;

    @Tolerate
    public ExecuteReqDTO() {
    }
}
