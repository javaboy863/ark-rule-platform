package com.ark.rule.platform.api.domain.fg.response;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.io.Serializable;

/**
 * 规则执行结果.
 *
 */
@Data
@Builder
public class ExecuteResultDTO implements Serializable {
    /**
     * 分组id
     */
    private Long groupId;
    /**
     * 命中规则id.
     */
    private Long hitRuleId;
    /**
     * 执行结果
     */
    private String result;

    @Tolerate
    public ExecuteResultDTO() {
    }
}
