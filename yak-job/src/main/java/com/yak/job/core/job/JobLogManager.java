/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.job.core.job;

import com.yak.job.common.dto.TaskLogPageQueryDTO;
import com.yak.job.common.vo.YakJobLogVO;

import java.util.List;

/**
 * 作业日志管理接口。
 */
public interface JobLogManager {
    List<YakJobLogVO> pageJobLogs(TaskLogPageQueryDTO var1);

    int getJobLogsCount(TaskLogPageQueryDTO var1);
}

