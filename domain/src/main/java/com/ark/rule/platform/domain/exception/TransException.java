package com.ark.rule.platform.domain.exception;

import com.ark.rule.platform.domain.enums.ResultEnum;
import org.apache.commons.lang3.StringUtils;

/**
 * 模块内部异常类.
 *
 */
public class TransException extends RuntimeException {
    /**
     * 错误枚举
     **/
    private ResultEnum resultEnum;

    /**
     * 详细错误信息
     **/
    private String detailErrorMsg;


    /**
     * 构造方法.
     *
     * @param resultEnum 错误枚举
     */
    public TransException(ResultEnum resultEnum) {
        this.resultEnum = resultEnum;
    }

    /**
     * 构造方法.
     *
     * @param resultEnum     错误枚举
     * @param detailErrorMsg 详细错误信息
     */
    public TransException(ResultEnum resultEnum, String detailErrorMsg) {
        this.resultEnum = resultEnum;
        this.detailErrorMsg = detailErrorMsg;
    }

    /**
     * 打印错误信息.
     *
     * @return ''
     */
    public String getPrintErrorMsg() {
        return StringUtils.isBlank(detailErrorMsg) ? resultEnum.getMsg() : detailErrorMsg;
    }

    /**
     * 返回业务错误信息.
     *
     * @return ''
     */
    public String getBizErrorMsg() {
        return resultEnum.getMsg();
    }

    /**
     * 错误code.
     *
     * @return ''
     */
    public int getErrorCode() {
        return this.resultEnum.getCode();
    }
}

