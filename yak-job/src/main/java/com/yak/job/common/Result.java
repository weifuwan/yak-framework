/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 *
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.JsonProcessingException
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package com.yak.job.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Result<T>
        extends BaseResult
        implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(Result.class);
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final long serialVersionUID = 3472961240718956029L;
    private T data;
    private String tips;

    public static Result build(ResultType resultType) {
        Result result = new Result();
        result.setCode(resultType.getCode());
        result.setMessage(resultType.getMessage());
        return result;
    }

    public static Result build(int code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMessage(msg);
        return result;
    }

    public static <T> Result<T> build(int code, String msg, T data) {
        Result<T> result = new Result<T>();
        result.setCode(code);
        result.setMessage(msg);
        result.setData(data);
        return result;
    }

    public static <T> Result<T> build(boolean succ, T data) {
        Result<T> result = new Result<T>();
        if (succ) {
            result.setCode(ResultType.SUCCESS.getCode());
            result.setMessage(ResultType.SUCCESS.getMessage());
            result.setData(data);
        } else {
            result.setCode(ResultType.FAIL.getCode());
            result.setMessage(ResultType.FAIL.getMessage());
        }
        return result;
    }

    public static Result build(boolean succ) {
        if (succ) {
            return Result.buildSucc();
        }
        return Result.buildFail();
    }

    public static Result buildSucc(String msg) {
        Result result = new Result();
        result.setCode(ResultType.SUCCESS.getCode());
        result.setMessage(msg);
        return result;
    }

    public static Result buildSucc() {
        Result result = new Result();
        result.setCode(ResultType.SUCCESS.getCode());
        result.setMessage(ResultType.SUCCESS.getMessage());
        return result;
    }

    public static <T> Result<T> buildSucc(T data) {
        Result<T> result = new Result<T>();
        result.setCode(ResultType.SUCCESS.getCode());
        result.setMessage(ResultType.SUCCESS.getMessage());
        result.setData(data);
        return result;
    }

    public static <T> Result<T> buildSucc(T data, String msg) {
        Result<T> result = new Result<T>();
        result.setCode(ResultType.SUCCESS.getCode());
        result.setMessage(msg);
        result.setData(data);
        return result;
    }

    public static Result buildFail(String failMsg) {
        Result result = new Result();
        result.setCode(ResultType.FAIL.getCode());
        result.setMessage(failMsg);
        return result;
    }

    public static Result buildFail() {
        Result result = new Result();
        result.setCode(ResultType.FAIL.getCode());
        result.setMessage(ResultType.FAIL.getMessage());
        return result;
    }

    public static Result buildParamIllegal(String msg) {
        Result result = new Result();
        result.setCode(ResultType.ILLEGAL_PARAMS.getCode());
        result.setMessage(msg);
        return result;
    }

    public static <T> Result<T> buildFrom(Result result) {
        Result<T> resultT = new Result<T>();
        resultT.setCode(result.getCode());
        resultT.setMessage(result.getMessage());
        return resultT;
    }

    public boolean success() {
        return this.getCode() != null && ResultType.SUCCESS.getCode() == this.getCode().intValue();
    }

    public boolean duplicate() {
        return this.getCode() != null && ResultType.DUPLICATION.getCode() == this.getCode().intValue();
    }

    public boolean failed() {
        return !this.success();
    }

    @Override
    public String toString() {
        String ret = "null";
        try {
            ret = mapper.writeValueAsString((Object) this);
        } catch (JsonProcessingException e) {
            logger.error("", (Throwable) e);
        }
        return ret;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getTips() {
        return this.tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Result)) {
            return false;
        }
        Result other = (Result) o;
        if (!other.canEqual(this)) {
            return false;
        }
        T this$data = this.getData();
        T other$data = other.getData();
        if (this$data == null ? other$data != null : !this$data.equals(other$data)) {
            return false;
        }
        String this$tips = this.getTips();
        String other$tips = other.getTips();
        return !(this$tips == null ? other$tips != null : !this$tips.equals(other$tips));
    }

    @Override
    protected boolean canEqual(Object other) {
        return other instanceof Result;
    }

    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        T $data = this.getData();
        result = result * 59 + ($data == null ? 43 : $data.hashCode());
        String $tips = this.getTips();
        result = result * 59 + ($tips == null ? 43 : $tips.hashCode());
        return result;
    }
}

