/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.job.core.job;

import com.yak.job.common.dto.TaskLogPageQueryDTO;
import com.yak.job.common.vo.LogIJobLogVO;
import java.util.List;

public interface JobLogManager {
    public List<LogIJobLogVO> pageJobLogs(TaskLogPageQueryDTO var1);

    public int getJobLogsCount(TaskLogPageQueryDTO var1);
}

