/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.job.common;

public enum ResultType {
    SUCCESS(0, "\u64cd\u4f5c\u6210\u529f"),
    FAIL(19999, "\u64cd\u4f5c\u5931\u8d25"),
    ILLEGAL_PARAMS(10000, "\u53c2\u6570\u9519\u8bef"),
    RESOURCE_NOT_READY(10001, "\u8d44\u6e90\u672a\u5c31\u7eea"),
    RESOURCE_PROCESSING(10002, "\u8d44\u6e90\u5ba1\u6279\u4e2d"),
    ES_OPERATE_ERROR(10003, "es\u64cd\u4f5c\u5931\u8d25"),
    DUPLICATION(10004, "\u6570\u636e\u5df2\u5b58\u5728"),
    OPERATE_FORBIDDEN_ERROR(10005, "\u65e0\u6743\u9650"),
    HDFS_ACCESS_ERROR(10006, "\u8bbf\u95eehdfs\u5931\u8d25"),
    LOGX_SQL_ERROR(10007, "logXSql\u64cd\u4f5c\u5931\u8d25"),
    LOGX_MODULE_ERROR(10008, "logXModule\u64cd\u4f5c\u5931\u8d25"),
    HTTP_REQ_ERROR(10009, "\u7b2c\u4e09\u65b9http\u8bf7\u6c42\u5f02\u5e38"),
    SAVE_MIDDLE_RESULT(20000, "\u4fdd\u5b58\u4e2d\u95f4\u7ed3\u679c"),
    MONITOR_NOT_EXIST(30000, "monitor not exist");

    private int code;
    private String message;

    private ResultType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}

