/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.didiglobal.logi.job.core.job;

import java.util.List;

public class JobContext {
    private String params;
    private List<String> allWorkerCodes;
    private String currentWorkerCode;

    public JobContext() {
    }

    public JobContext(String params, List<String> allWorkerCodes, String currentWorkerCode) {
        this.params = params;
        this.allWorkerCodes = allWorkerCodes;
        this.currentWorkerCode = currentWorkerCode;
    }

    public String getParams() {
        return this.params;
    }

    public List<String> getAllWorkerCodes() {
        return this.allWorkerCodes;
    }

    public String getCurrentWorkerCode() {
        return this.currentWorkerCode;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public void setAllWorkerCodes(List<String> allWorkerCodes) {
        this.allWorkerCodes = allWorkerCodes;
    }

    public void setCurrentWorkerCode(String currentWorkerCode) {
        this.currentWorkerCode = currentWorkerCode;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof JobContext)) {
            return false;
        }
        JobContext other = (JobContext)o;
        if (!other.canEqual(this)) {
            return false;
        }
        String this$params = this.getParams();
        String other$params = other.getParams();
        if (this$params == null ? other$params != null : !this$params.equals(other$params)) {
            return false;
        }
        List<String> this$allWorkerCodes = this.getAllWorkerCodes();
        List<String> other$allWorkerCodes = other.getAllWorkerCodes();
        if (this$allWorkerCodes == null ? other$allWorkerCodes != null : !((Object)this$allWorkerCodes).equals(other$allWorkerCodes)) {
            return false;
        }
        String this$currentWorkerCode = this.getCurrentWorkerCode();
        String other$currentWorkerCode = other.getCurrentWorkerCode();
        return !(this$currentWorkerCode == null ? other$currentWorkerCode != null : !this$currentWorkerCode.equals(other$currentWorkerCode));
    }

    protected boolean canEqual(Object other) {
        return other instanceof JobContext;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $params = this.getParams();
        result = result * 59 + ($params == null ? 43 : $params.hashCode());
        List<String> $allWorkerCodes = this.getAllWorkerCodes();
        result = result * 59 + ($allWorkerCodes == null ? 43 : ((Object)$allWorkerCodes).hashCode());
        String $currentWorkerCode = this.getCurrentWorkerCode();
        result = result * 59 + ($currentWorkerCode == null ? 43 : $currentWorkerCode.hashCode());
        return result;
    }

    public String toString() {
        return "JobContext(params=" + this.getParams() + ", allWorkerCodes=" + this.getAllWorkerCodes() + ", currentWorkerCode=" + this.getCurrentWorkerCode() + ")";
    }
}

