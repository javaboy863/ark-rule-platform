package com.ark.rule.platform.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 参数验证工具.
 *
 */
@Slf4j
public class AssertUtil {

    /**
     * 验证参数不能为空.
     *
     * @param value    参数值
     * @param errorMsg 错误信息
     */
    public static void assertNotBlank(String value, String errorMsg) {
        if (StringUtils.isBlank(value)) {
            log.error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }
    }
}
