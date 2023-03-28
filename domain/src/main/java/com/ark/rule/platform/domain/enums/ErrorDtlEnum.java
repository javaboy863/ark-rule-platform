package com.ark.rule.platform.domain.enums;

/**
 * 描述类的功能.
 *
 */
public enum ErrorDtlEnum {
    /**
     * 正常.
     */
    ERR_OK(0, "ok", ResultEnum.SUCCESS),

    /**
     * 参数校验错误.
     */
    PARA_NOT_VALID(1001, "参数不合法", ResultEnum.PARAM_TYPE_ERROR),
    /**
     * 参数为空.
     */
    PARA_NOT_BLANK(1002, "必填参数不能为空", ResultEnum.PARAM_ERROR),


    /**
     * 数据库操作错误.
     */
    OPERATE_DATABASE_ERROR(1004, "操作数据库错误", ResultEnum.ERR_OPERATE_DATABASE),

    // ~~~~~~~~~~~~~~~~~~~~~~ 3001~9999之间序号表示业务错误 ~~~~~~~~~~~~~~~~~~~~
    /**
     * 配置异常.
     */
    CONFIG_ERROR(3001, "配置异常", ResultEnum.CONFIG_ERROR),
    /**
     * 未知异常.
     */
    UNKNOWN_ERROR(9999, "未知异常", ResultEnum.SYSTEM_ERROR);

    // ~~~~~~~~~~~~~~~~~~ 枚举的成员变量 ~~~~~~~~~~~~~~~~~~~
    /**
     * 错误码
     */
    private int errCode;

    /**
     * 错误描述
     */
    private String errDesc;

    /**
     * 返回码的枚举
     **/
    private ResultEnum resultEnum;

    /**
     * 构造方法
     *
     * @param errCode    ""
     * @param errDesc    ''
     * @param resultEnum ''
     */
    ErrorDtlEnum(int errCode, String errDesc, ResultEnum resultEnum) {
        this.errCode = errCode;
        this.errDesc = errDesc;
        this.resultEnum = resultEnum;
    }

    public ResultEnum getResultCodeEnum() {
        return resultEnum;
    }

    public int getErrCode() {
        return errCode;
    }

    public String getErrDesc() {
        return errDesc;
    }
}
