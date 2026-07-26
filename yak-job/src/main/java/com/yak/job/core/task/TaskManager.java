/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.job.core.task;

import com.yak.job.common.Result;
import com.yak.job.common.domain.YakTask;
import com.yak.job.common.dto.YakTaskDTO;
import com.yak.job.common.dto.TaskPageQueryDTO;

import java.util.List;

public interface TaskManager {
    public Result delete(String var1);

    public boolean update(YakTaskDTO var1);

    public List<YakTask> nextTriggers(Long var1);

    public List<YakTask> nextTriggers(Long var1, Long var2);

    public void submit(List<YakTask> var1);

    public Result execute(String var1, Boolean var2);

    public void execute(YakTask var1, Boolean var2);

    public int stopAll();

    public Result<Boolean> updateTaskStatus(String var1, int var2);

    public Result<Boolean> copy(String var1, String var2, List<String> var3, String var4);

    public Result<Boolean> updateWorkIpsParam(String var1, List<String> var2, String var3);

    public List<YakTask> getAllRuning();

    public int pagineTaskConut(TaskPageQueryDTO var1);

    public List<YakTask> getPagineList(TaskPageQueryDTO var1);

    public Result<Boolean> release(String var1, String var2);

    public YakTask getByCode(String var1);
}

