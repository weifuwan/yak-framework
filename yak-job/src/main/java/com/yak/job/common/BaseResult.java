/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 *
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModelProperty
 */
package com.yak.job.common;

import io.swagger.annotations.ApiModelProperty;

public class BaseResult {
    @ApiModelProperty(value = "\u8fd4\u56de\u4fe1\u606f")
    protected String message;
    @ApiModelProperty(value = "\u8fd4\u56de\u7f16\u53f7\uff08200\u6210\u529f\uff0c\u5176\u4ed6\u89c1message\uff09")
    protected Integer code;

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BaseResult)) {
            return false;
        }
        BaseResult other = (BaseResult) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$code = this.getCode();
        Integer other$code = other.getCode();
        if (this$code == null ? other$code != null : !((Object) this$code).equals(other$code)) {
            return false;
        }
        String this$message = this.getMessage();
        String other$message = other.getMessage();
        return !(this$message == null ? other$message != null : !this$message.equals(other$message));
    }

    protected boolean canEqual(Object other) {
        return other instanceof BaseResult;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $code = this.getCode();
        result = result * 59 + ($code == null ? 43 : ((Object) $code).hashCode());
        String $message = this.getMessage();
        result = result * 59 + ($message == null ? 43 : $message.hashCode());
        return result;
    }

    public String toString() {
        return "BaseResult(message=" + this.getMessage() + ", code=" + this.getCode() + ")";
    }
}

