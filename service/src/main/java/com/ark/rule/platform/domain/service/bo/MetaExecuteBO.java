package com.ark.rule.platform.domain.service.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 规则计算元数据对象.
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetaExecuteBO {
    /**
     * 元数据code.
     */
    private String metaCode;
    /**
     * 元数据请求值.
     */
    private String reqValue;
    /**
     * 元数据默认值.
     */
    private String defaultValue;
    /**
     * 操作符
     */
    private String operator;
    /**
     * 元数据值对应规则列表.
     */
    private Map<String, List<Long>> metaValueRuleMap;
}

