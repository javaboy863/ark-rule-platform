package com.ark.rule.platform.api;

import java.io.Serializable;

public class Result<T> implements Serializable {

    private int code = 0;
    private String msg = "";
    private static final int SUCCESS_CODE = 0;

    private T data;

    public Result() {
    }

    public void setData(T data) {
        this.data = data;
    }

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public boolean isSuccess() {
        return this.code == 0;
    }

    public static Result wrapError(int code, String msg) {
        return new Result(code, msg);
    }

    public static <T> Result<T> wrapSuccess(T data) {
        Result<T> result = new Result();
        result.setData(data);
        return result;
    }

    public static <T> Result<T> wrapSuccess(int code,T data) {
        Result<T> result = new Result();
        result.setData(data);
        result.setCode(code);
        return result;
    }

    public T getData() {
        return this.data;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Result{" +
                "data=" + data +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
