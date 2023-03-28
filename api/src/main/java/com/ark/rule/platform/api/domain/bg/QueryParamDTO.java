package com.ark.rule.platform.api.domain.bg;

import com.ark.rule.platform.api.common.RuleConstant;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.io.Serializable;

/**
 * 查询参数.
 *
 */
@Data
@Builder
public class QueryParamDTO implements Serializable {

    /**
     * 名称
     */
    private String name;
    /**
     * 编码
     */
    private String code;
    /**
     * 每页记录数
     */
    private Integer size;
    /**
     * 第几页
     */
    private Integer index;

    @Tolerate
    public QueryParamDTO() {
    }

    /**
     * 参数校验.
     */
    public void check() {
        if (size == null || size <= 0 || size > RuleConstant.PAGE_MAX_SIZE) {
            throw new IllegalArgumentException("size 异常:" + size);
        }
        if (index == null || index <= 0) {
            throw new IllegalArgumentException("index 异常:" + index);
        }
    }
}
