/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.didiglobal.logi.security.common;

import com.didiglobal.logi.security.common.BaseResult;
import com.didiglobal.logi.security.common.PagingData;
import com.didiglobal.logi.security.common.enums.ResultCode;
import com.didiglobal.logi.security.exception.LogiSecurityException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="\u5206\u9875\u7edf\u4e00\u8fd4\u56de\u683c\u5f0f")
public class PagingResult<T>
extends BaseResult {
    @ApiModelProperty(value="\u8fd4\u56de\u5206\u9875\u57fa\u672c\u4fe1\u606f")
    private PagingData<T> data;

    private PagingResult(Integer code) {
        this.code = code;
    }

    private PagingResult(Integer code, String msg) {
        this.code = code;
        this.message = msg;
    }

    public static <T> PagingResult<T> success(PagingData<T> data) {
        PagingResult<T> ret = new PagingResult<T>(ResultCode.SUCCESS.getCode());
        ret.setMessage(ResultCode.SUCCESS.getMessage());
        ret.setData(data);
        return ret;
    }

    public static <T> PagingResult<T> success() {
        return new PagingResult<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage());
    }

    public static <T> PagingResult<T> fail(ResultCode resultCode) {
        PagingResult<T> ret = new PagingResult<T>(resultCode.getCode());
        ret.setMessage(resultCode.getMessage());
        return ret;
    }

    public static <T> PagingResult<T> fail(Integer code, String msg) {
        PagingResult<T> ret = new PagingResult<T>(code);
        ret.setMessage(msg);
        return ret;
    }

    public static <T> PagingResult<T> fail(String msg) {
        PagingResult<T> ret = new PagingResult<T>(ResultCode.COMMON_FAIL.getCode());
        ret.setMessage(msg);
        return ret;
    }

    public static <T> PagingResult<T> fail(LogiSecurityException e) {
        String[] s = e.getMessage().split("-", 2);
        return PagingResult.fail(Integer.parseInt(s[0]), s[1]);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PagingResult)) {
            return false;
        }
        PagingResult other = (PagingResult)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        PagingData<T> this$data = this.getData();
        PagingData<T> other$data = other.getData();
        return !(this$data == null ? other$data != null : !((Object)this$data).equals(other$data));
    }

    @Override
    protected boolean canEqual(Object other) {
        return other instanceof PagingResult;
    }

    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = super.hashCode();
        PagingData<T> $data = this.getData();
        result = result * 59 + ($data == null ? 43 : ((Object)$data).hashCode());
        return result;
    }

    public PagingData<T> getData() {
        return this.data;
    }

    public void setData(PagingData<T> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PagingResult(data=" + this.getData() + ")";
    }
}

