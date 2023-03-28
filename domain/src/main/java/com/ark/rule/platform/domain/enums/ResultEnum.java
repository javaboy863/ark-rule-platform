
package com.ark.rule.platform.domain.enums;

/**
 * 返回结果描述.
 *
 */
public enum ResultEnum {

    /**
     * 成功.
     */
    SUCCESS(0, "成功"),

    /**
     * 系统错误.
     */
    SYSTEM_ERROR(1, "系统错误"),

    /**
     * 参数错误.
     */
    PARAM_ERROR(2, "参数错误"),

    /**
     * 签名错误.
     */
    SIGN_ERROR(3, "签名错误"),
    /**
     * 参数类型或值异常.
     */
    PARAM_TYPE_ERROR(4, "参数类型异常"),
    /**
     * 操作数据库异常.
     */
    ERR_OPERATE_DATABASE(5, "操作数据库异常"),
    /**
     * 配置异常.
     */
    CONFIG_ERROR(6, "配置异常"),
    /**
     * 计算异常.
     */
    EXECUTOR_ERROR(10001, "计算异常"),
    /**
     * 规则组不存在.
     */
    NO_RULE_GROUP(10002, "规则组不存在"),
    /**
     * 非法业务线.
     */
    NO_BUSINESS(10003, "非法业务类型"),
    /**
     * 规则元数据缺失.
     */
    NO_RULE_META(10004, "规则元数据缺失"),
    /**
     * 非法元数据code.
     */
    NO_META_CODE(10005, "非法元数据code"),
    /**
     * 规则组无规则信息.
     */
    GROUP_NO_RULE(10006, "规则组无规则信息"),
    /**
     * 元数据请求值缺失.
     */
    NO_META_VALUE(10007, "元数据请求值缺失");

    private int code;

    private String msg;

    ResultEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
