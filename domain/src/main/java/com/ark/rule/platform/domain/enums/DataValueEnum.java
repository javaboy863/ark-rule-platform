
package com.ark.rule.platform.domain.enums;

/**
 * 数据有效性枚举.
 *
 */
public enum DataValueEnum {

    /**
     * 已删除.
     */
    DELETA(1, "已删除"),

    /**
     * 未删除.
     */
    NO_DELETA(0, "未删除");

    private int code;
    private String desc;

    DataValueEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}

