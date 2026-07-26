/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.didiglobal.logi.job.common;

import java.io.Serializable;

public class TaskResult
implements Serializable {
    public static final long serialVersionUID = 42L;
    private int code;
    private String message;
    public static final int SUCCESS_CODE = 1;
    public static final int RUNNING_CODE = 0;
    public static final int FAIL_CODE = -1;
    public static final TaskResult SUCCESS = new TaskResult(1, "scuucessed");
    public static final TaskResult FAIL = new TaskResult(-1, "failed");

    public TaskResult() {
    }

    public TaskResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TaskResult)) {
            return false;
        }
        TaskResult other = (TaskResult)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getCode() != other.getCode()) {
            return false;
        }
        String this$message = this.getMessage();
        String other$message = other.getMessage();
        return !(this$message == null ? other$message != null : !this$message.equals(other$message));
    }

    protected boolean canEqual(Object other) {
        return other instanceof TaskResult;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getCode();
        String $message = this.getMessage();
        result = result * 59 + ($message == null ? 43 : $message.hashCode());
        return result;
    }

    public String toString() {
        return "TaskResult(code=" + this.getCode() + ", message=" + this.getMessage() + ")";
    }
}

