package com.ark.rule.platform.api.domain.fg.request;

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
public class BatchExecuteReqDTO implements Serializable {

    /**
     * 业务线编码
     */
    private String bizCode;
    /**
     * 要执行的数据
     */
    private List<ExecuteItemDTO> executeItems;

    @Tolerate
    public BatchExecuteReqDTO() {
    }
}
