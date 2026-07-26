/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.job.core.task;

import com.yak.job.common.Result;
import com.yak.job.common.domain.LogITask;
import com.yak.job.common.dto.LogITaskDTO;
import com.yak.job.common.dto.TaskPageQueryDTO;
import java.util.List;

public interface TaskManager {
    public Result delete(String var1);

    public boolean update(LogITaskDTO var1);

    public List<LogITask> nextTriggers(Long var1);

    public List<LogITask> nextTriggers(Long var1, Long var2);

    public void submit(List<LogITask> var1);

    public Result execute(String var1, Boolean var2);

    public void execute(LogITask var1, Boolean var2);

    public int stopAll();

    public Result<Boolean> updateTaskStatus(String var1, int var2);

    public Result<Boolean> copy(String var1, String var2, List<String> var3, String var4);

    public Result<Boolean> updateWorkIpsParam(String var1, List<String> var2, String var3);

    public List<LogITask> getAllRuning();

    public int pagineTaskConut(TaskPageQueryDTO var1);

    public List<LogITask> getPagineList(TaskPageQueryDTO var1);

    public Result<Boolean> release(String var1, String var2);

    public LogITask getByCode(String var1);
}

