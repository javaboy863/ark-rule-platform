
package com.ark.rule.platform.domain.dto.inner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 业务线配置信息.
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IBusinessDTO implements Serializable {
    private static final long serialVersionUID = 4051599434157551819L;

    private Long id;

    /**
     * 业务线名称
     */
    private String bizName;

    /**
     * 业务线编码
     */
    private String bizCode;

    /**
     * 0:未删除,1:已删除
     */
    private Integer isDelete;
}

