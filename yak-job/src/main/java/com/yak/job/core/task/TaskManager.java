/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.job.core.task;

import com.yak.job.common.Result;
import com.yak.job.common.domain.YakTask;
import com.yak.job.common.dto.YakTaskDTO;
import com.yak.job.common.dto.TaskPageQueryDTO;

import java.util.List;

/**
 * 任务管理接口。
 */
public interface TaskManager {
    Result delete(String var1);

    boolean update(YakTaskDTO var1);

    List<YakTask> nextTriggers(Long var1);

    List<YakTask> nextTriggers(Long var1, Long var2);

    void submit(List<YakTask> var1);

    Result execute(String var1, Boolean var2);

    void execute(YakTask var1, Boolean var2);

    int stopAll();

    Result<Boolean> updateTaskStatus(String var1, int var2);

    Result<Boolean> copy(String var1, String var2, List<String> var3, String var4);

    Result<Boolean> updateWorkIpsParam(String var1, List<String> var2, String var3);

    List<YakTask> getAllRuning();

    int pagineTaskConut(TaskPageQueryDTO var1);

    List<YakTask> getPagineList(TaskPageQueryDTO var1);

    Result<Boolean> release(String var1, String var2);

    YakTask getByCode(String var1);
}

