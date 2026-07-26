/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.job.core.task;

import com.yak.job.common.vo.YakTaskLockVO;

import java.util.List;

/**
 * 任务锁服务接口。
 */
public interface TaskLockService {
    Boolean tryAcquire(String var1);

    Boolean tryAcquire(String var1, String var2, Long var3);

    Boolean tryRelease(String var1);

    Boolean tryRelease(String var1, String var2);

    List<YakTaskLockVO> getAll();

    void renewAll();
}

