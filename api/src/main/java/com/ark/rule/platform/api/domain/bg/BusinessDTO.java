
package com.ark.rule.platform.api.domain.bg;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.io.Serializable;
import java.util.Date;

/**
 * 业务线实体.
 *
 */
@Data
@Builder
public class BusinessDTO implements Serializable {

    /**
     * 主键
     */
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
    public BusinessDTO() {
    }
}
