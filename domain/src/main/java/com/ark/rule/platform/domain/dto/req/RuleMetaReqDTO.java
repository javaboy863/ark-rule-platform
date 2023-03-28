
package com.ark.rule.platform.domain.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 规则元数据配置请求.
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleMetaReqDTO implements Serializable {
    private static final long serialVersionUID = -3131973695068026125L;
    /**
     * 元数据编码
     */
    private String code;

    /**
     * 元数据值
     */
    private String value;
}

