package com.ark.rule.platform.api.domain.fg.request;

import com.ark.rule.platform.api.domain.fg.RuleReqDTO;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.io.Serializable;
import java.util.List;

/**
 * 描述类的功能.
 *
 */
@Data
@Builder
public class ExecuteItemDTO implements Serializable {
    /**
     * 规则组id
     */
    private Long groupId;
    /**
     * 请求的元数据
     */
    private List<RuleReqDTO> metas;

    @Tolerate
    public ExecuteItemDTO() {
    }
}
