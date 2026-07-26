/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.job.core.task;

import com.yak.job.common.vo.LogITaskLockVO;
import java.util.List;

public interface TaskLockService {
    public Boolean tryAcquire(String var1);

    public Boolean tryAcquire(String var1, String var2, Long var3);

    public Boolean tryRelease(String var1);

    public Boolean tryRelease(String var1, String var2);

    public List<LogITaskLockVO> getAll();

    public void renewAll();
}

