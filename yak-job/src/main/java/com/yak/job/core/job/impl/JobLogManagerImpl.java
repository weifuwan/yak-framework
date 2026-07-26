/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 *
 * Could not load the following classes:
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 *  org.springframework.util.CollectionUtils
 */
package com.yak.job.core.job.impl;

import com.yak.job.YakJobProperties;
import com.yak.job.common.domain.YakTask;
import com.yak.job.common.dto.TaskLogPageQueryDTO;
import com.yak.job.common.po.YakJobLogPO;
import com.yak.job.common.vo.YakJobLogVO;
import com.yak.job.core.job.JobLogManager;
import com.yak.job.core.task.TaskManager;
import com.yak.job.mapper.YakJobLogMapper;
import com.yak.job.utils.BeanUtil;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class JobLogManagerImpl
        implements JobLogManager {
    private static final String SORT_DESC = "desc";
    private static final String SORT_ASC = "asc";
    private static final String SORT_ID = "id";
    private static final String SORT_STATUS = "status";
    private static final String SORT_RESULT = "result";
    private static final String SORT_CREATE_TIME = "create_time";
    private static final String SORT_START_TIME = "start_time";
    private static final String SORT_END_TIME = "end_time";
    private TaskManager taskManager;
    private YakJobLogMapper yakJobLogMapper;
    private YakJobProperties yakJobProperties;

    @Autowired
    public JobLogManagerImpl(TaskManager taskManager, YakJobLogMapper yakJobLogMapper, YakJobProperties yakJobProperties) {
        this.taskManager = taskManager;
        this.yakJobLogMapper = yakJobLogMapper;
        this.yakJobProperties = yakJobProperties;
    }

    @Override
    public List<YakJobLogVO> pageJobLogs(TaskLogPageQueryDTO dto) {
        List<YakJobLogPO> yakJobLogPOS;
        HashMap longYakTaskMap = new HashMap();
        Timestamp beginTimestamp = null;
        Timestamp endTimestamp = null;
        if (null != dto.getBeginTime()) {
            beginTimestamp = new Timestamp(dto.getBeginTime());
        }
        if (null != dto.getEndTime()) {
            endTimestamp = new Timestamp(dto.getEndTime());
        }
        if (CollectionUtils.isEmpty(yakJobLogPOS = this.yakJobLogMapper.pagineListByCondition(this.yakJobProperties.getAppName(), dto.getTaskId(), dto.getTaskDesc(), dto.getTaskStatus(), (dto.getPage() - 1) * dto.getSize(), dto.getSize(), this.genSortName(dto.getSortName()), this.genSort(dto.getSortAsc()), beginTimestamp, endTimestamp))) {
            return null;
        }
        return yakJobLogPOS.stream().map(yakJobLogPO -> {
            YakJobLogVO yakJobLogVO = BeanUtil.convertTo(yakJobLogPO, YakJobLogVO.class);
            YakTask yakTask = (YakTask) longYakTaskMap.get(yakJobLogPO.getTaskId());
            if (null == yakTask) {
                yakTask = this.taskManager.getByCode(yakJobLogPO.getTaskCode());
                longYakTaskMap.put(yakJobLogPO.getTaskId(), yakTask);
            }
            List<String> ips = yakTask.getTaskWorkers().stream().map(w -> w.getIp()).collect(Collectors.toList());
            yakJobLogVO.setAllWorkerIps(ips);
            yakJobLogVO.setTaskName(yakTask.getTaskName());
            return yakJobLogVO;
        }).collect(Collectors.toList());
    }

    @Override
    public int getJobLogsCount(TaskLogPageQueryDTO dto) {
        Timestamp beginTimestamp = null;
        Timestamp endTimestamp = null;
        if (null != dto.getBeginTime()) {
            beginTimestamp = new Timestamp(dto.getBeginTime());
        }
        if (null != dto.getEndTime()) {
            endTimestamp = new Timestamp(dto.getEndTime());
        }
        return this.yakJobLogMapper.pagineCountByCondition(this.yakJobProperties.getAppName(), dto.getTaskId(), dto.getTaskDesc(), dto.getTaskStatus(), beginTimestamp, endTimestamp);
    }

    private String genSortName(String sortName) {
        if (SORT_STATUS.equals(sortName)) {
            return SORT_STATUS;
        }
        if (SORT_RESULT.equals(sortName)) {
            return SORT_RESULT;
        }
        if (SORT_CREATE_TIME.equals(sortName)) {
            return SORT_CREATE_TIME;
        }
        if (SORT_START_TIME.equals(sortName)) {
            return SORT_START_TIME;
        }
        if (SORT_END_TIME.equals(sortName)) {
            return SORT_END_TIME;
        }
        return SORT_ID;
    }

    private String genSort(String sortAsc) {
        if (SORT_DESC.equals(sortAsc)) {
            return SORT_DESC;
        }
        return SORT_ASC;
    }
}

