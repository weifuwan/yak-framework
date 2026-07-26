/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.didiglobal.logi.job.common;

import com.didiglobal.logi.job.common.BaseResult;
import com.didiglobal.logi.job.common.PagingData;
import com.didiglobal.logi.job.common.ResultType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description="\u5206\u9875\u7edf\u4e00\u8fd4\u56de\u683c\u5f0f")
public class PagingResult<T>
extends BaseResult {
    @ApiModelProperty(value="\u8fd4\u56de\u5206\u9875\u57fa\u672c\u4fe1\u606f")
    private PagingData<T> data;

    public PagingResult(PagingData<T> data) {
        this.data = data;
    }

    public PagingResult(List<T> records, long total, long pageNo, long pageSize) {
        this.data = new PagingData<T>(records, total, pageNo, pageSize);
    }

    public static <T> PagingResult<T> buildSucc(List<T> records, long total, long pageNo, long pageSize) {
        PagingResult<T> paginationResult = new PagingResult<T>(records, total, pageNo, pageSize);
        paginationResult.setCode(ResultType.SUCCESS.getCode());
        paginationResult.setMessage(ResultType.SUCCESS.getMessage());
        return paginationResult;
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

