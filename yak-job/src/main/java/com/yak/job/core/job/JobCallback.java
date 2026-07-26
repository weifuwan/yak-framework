/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.job.core.job;

import com.yak.job.common.domain.YakJob;

/**
 * 作业执行回调接口。
 */
public interface JobCallback {
    void callback(YakJob var1);
}

