
package com.ark.rule.platform.api.domain.bg;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.io.Serializable;
import java.util.List;

/**
 * 列表查询结果.
 *
 * @param <E> 实际的数据实体
 */
@Data
@Builder
public class PageListDTO<E> implements Serializable {
    /**
     * 总数量
     */
    private Integer totalSize;
    /**
     * 数据列表
     */
    private List<E> list;

    @Tolerate
    public PageListDTO() {
    }
}
