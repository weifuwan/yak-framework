/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.job.common.po;

import java.io.Serializable;

public class YakWorkerBlacklistPO
extends BasePO
implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String workerCode;

    public Long getId() {
        return this.id;
    }

    public String getWorkerCode() {
        return this.workerCode;
    }

    public YakWorkerBlacklistPO setId(Long id) {
        this.id = id;
        return this;
    }

    public YakWorkerBlacklistPO setWorkerCode(String workerCode) {
        this.workerCode = workerCode;
        return this;
    }

    @Override
    public String toString() {
        return "YakWorkerBlacklistPO(id=" + this.getId() + ", workerCode=" + this.getWorkerCode() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof YakWorkerBlacklistPO)) {
            return false;
        }
        YakWorkerBlacklistPO other = (YakWorkerBlacklistPO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$id = this.getId();
        Long other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        String this$workerCode = this.getWorkerCode();
        String other$workerCode = other.getWorkerCode();
        return !(this$workerCode == null ? other$workerCode != null : !this$workerCode.equals(other$workerCode));
    }

    @Override
    protected boolean canEqual(Object other) {
        return other instanceof YakWorkerBlacklistPO;
    }

    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        String $workerCode = this.getWorkerCode();
        result = result * 59 + ($workerCode == null ? 43 : $workerCode.hashCode());
        return result;
    }
}

