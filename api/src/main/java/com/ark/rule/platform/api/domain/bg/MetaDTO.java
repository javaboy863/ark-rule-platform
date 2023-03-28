
package com.ark.rule.platform.api.domain.bg;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.io.Serializable;
import java.util.Date;

/**
 * 元数据dto.
 *
 */
@Data
@Builder
public class MetaDTO implements Serializable {

    /**
     * 主键
     */
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
     *
     * @mbg.generated 2019-12-30 15:24:29
     */
    private String remark;

    /**
     * 0:未删除,1:已删除
     */
    private Integer isDelete;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 更新人
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

    @Tolerate
    public MetaDTO() {
    }
}
