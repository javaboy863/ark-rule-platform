
package com.ark.rule.platform.domain.dto.inner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 元数据配置信息.
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IMetaDTO implements Serializable {
    private static final long serialVersionUID = 1264672715205380010L;
    private Long id;

    /**
     * 元数据名称
     */
    private String metaName;

    /**
     * 元数据编码
     */
    private String metaCode;

    /**
     * 可用的操作符
     */
    private String limitOperator;

    /**
     * 值类型:1-字符串,2-数字
     */
    private Integer valueType;

    /**
     * 默认值
     */
    private String defaultValue;
    /**
     * 元数据描述. remark
     */
    private String remark;

    /**
     * 0:未删除,1:已删除
     */
    private Integer isDelete;
}

