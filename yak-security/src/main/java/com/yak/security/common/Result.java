/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.yak.security.common;

import com.yak.security.common.enums.ResultCode;
import com.yak.security.exception.LogiSecurityException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="\u7edf\u4e00\u8fd4\u56de\u683c\u5f0f")
public class Result<T>
extends BaseResult {
    @ApiModelProperty(value="\u8fd4\u56de\u6570\u636e")
    protected T data;

    public boolean successed() {
        return this.getCode() != null && ResultCode.SUCCESS.getCode().equals(this.getCode());
    }

    public boolean duplicate() {
        return this.getCode() != null && ResultCode.RESOURCE_DUPLICATION.getCode().equals(this.getCode());
    }

    public boolean failed() {
        return !this.successed();
    }

    public Result() {
    }

    private Result(Integer code) {
        this.code = code;
    }

    private Result(Integer code, String msg) {
        this.code = code;
        this.message = msg;
    }

    public static <T> Result<T> build(boolean succ) {
        if (succ) {
            return Result.success();
        }
        return Result.fail();
    }

    public static <T> Result<T> success(T data) {
        Result<T> ret = new Result<T>(ResultCode.SUCCESS.getCode());
        ret.setMessage(ResultCode.SUCCESS.getMessage());
        ret.setData(data);
        return ret;
    }

    public static <T> Result<T> success() {
        return new Result<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage());
    }

    public static <T> Result<T> fail(ResultCode resultCode) {
        Result<T> ret = new Result<T>(resultCode.getCode());
        ret.setMessage(resultCode.getMessage());
        return ret;
    }

    public static <T> Result<T> fail(Integer code, String msg) {
        Result<T> ret = new Result<T>(code);
        ret.setMessage(msg);
        return ret;
    }

    public static <T> Result<T> fail(String msg) {
        Result<T> ret = new Result<T>(ResultCode.COMMON_FAIL.getCode());
        ret.setMessage(msg);
        return ret;
    }

    public static <T> Result<T> fail() {
        Result<T> result = new Result<T>();
        result.setCode(ResultCode.COMMON_FAIL.getCode());
        result.setMessage(ResultCode.COMMON_FAIL.getMessage());
        return result;
    }

    public static <T> Result<T> fail(LogiSecurityException e) {
        String[] s = e.getMessage().split("-", 2);
        return Result.fail(Integer.parseInt(s[0]), s[1]);
    }

    public static <T> Result<T> buildSucc(T data) {
        Result<T> result = new Result<T>();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMessage(ResultCode.SUCCESS.getMessage());
        result.setData(data);
        return result;
    }

    public static <T> Result<T> buildFrom(Result<? extends Object> result) {
        Result<T> resultT = new Result<T>();
        resultT.setCode(result.getCode());
        resultT.setMessage(result.getMessage());
        return resultT;
    }

    public static <T> Result<T> buildParamIllegal(String msg) {
        Result<T> result = new Result<T>();
        result.setCode(ResultCode.PARAM_NOT_VALID.getCode());
        result.setMessage(ResultCode.PARAM_NOT_VALID.getMessage() + ":" + msg + "\uff0c\u8bf7\u68c0\u67e5\u540e\u518d\u63d0\u4ea4\uff01");
        return result;
    }

    public static <T> Result<T> buildNotExist(String msg) {
        Result<T> result = new Result<T>();
        result.setCode(ResultCode.RESOURCE_TYPE_NOT_EXISTS.getCode());
        result.setMessage(msg);
        return result;
    }

    public static <T> Result<T> buildDuplicate(String msg) {
        Result<T> result = new Result<T>();
        result.setCode(ResultCode.RESOURCE_DUPLICATION.getCode());
        result.setMessage(msg);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Result)) {
            return false;
        }
        Result other = (Result)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        T this$data = this.getData();
        T other$data = other.getData();
        return !(this$data == null ? other$data != null : !this$data.equals(other$data));
    }

    @Override
    protected boolean canEqual(Object other) {
        return other instanceof Result;
    }

    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = super.hashCode();
        T $data = this.getData();
        result = result * 59 + ($data == null ? 43 : $data.hashCode());
        return result;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result(data=" + this.getData() + ")";
    }
}

