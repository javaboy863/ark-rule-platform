package com.ark.rule.platform.common.enums;

/**
 * 元数据值类型枚举.
 *
 */
public enum MetaValueTypeEnum {
    /**
     * 字符串.
     */
    STRING(1, "字符串"),
    /**
     * 数字.
     */
    NUMBER(2, "数字");

    /**
     * 根据code获取值类型.
     *
     * @param typeCode ''
     * @return ''
     */
    public static MetaValueTypeEnum getValueTypeByCode(Integer typeCode) {
        if (typeCode == null) {
            return null;
        }
        for (MetaValueTypeEnum type : MetaValueTypeEnum.values()) {
            if (type.code == typeCode) {
                return type;
            }
        }
        return null;
    }

    /**
     * 类型.
     */
    private int code;
    /**
     * 描述.
     */
    private String desc;

    MetaValueTypeEnum(int code, String desc) {
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

