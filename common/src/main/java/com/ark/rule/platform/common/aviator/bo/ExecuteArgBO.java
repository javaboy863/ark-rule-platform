
package com.ark.rule.platform.common.aviator.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 规则计算function入参.
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteArgBO {
    /**
     * 元数据请求入参.
     */
    private String reqArg;
    /**
     * 元数据配置参数.
     */
    private String configArg;
    /**
     * 元数据默认通用参数.
     */
    private String defaultArg;
}

