/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 *  org.springframework.util.CollectionUtils
 */
package com.didiglobal.logi.job.core.task;

import com.didiglobal.logi.job.LogIJobProperties;
import com.didiglobal.logi.job.common.po.LogITaskLockPO;
import com.didiglobal.logi.job.common.vo.LogITaskLockVO;
import com.didiglobal.logi.job.core.WorkerSingleton;
import com.didiglobal.logi.job.core.task.TaskLockService;
import com.didiglobal.logi.job.mapper.LogITaskLockMapper;
import com.didiglobal.logi.job.utils.BeanUtil;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class TaskLockServiceImpl
implements TaskLockService {
    private static final Logger logger = LoggerFactory.getLogger(TaskLockServiceImpl.class);
    private static final Long EXPIRE_TIME_SECONDS = 300L;
    private LogITaskLockMapper logITaskLockMapper;
    private LogIJobProperties logIJobProperties;

    @Autowired
    public TaskLockServiceImpl(LogITaskLockMapper logITaskLockMapper, LogIJobProperties logIJobProperties) {
        this.logITaskLockMapper = logITaskLockMapper;
        this.logIJobProperties = logIJobProperties;
    }

    @Override
    public Boolean tryAcquire(String taskCode) {
        return this.tryAcquire(taskCode, WorkerSingleton.getInstance().getLogIWorker().getWorkerCode(), EXPIRE_TIME_SECONDS);
    }

    @Override
    public Boolean tryAcquire(String taskCode, String workerCode, Long expireTime) {
        List<LogITaskLockPO> logITaskLockPOList = this.logITaskLockMapper.selectByTaskCode(taskCode, this.logIJobProperties.getAppName());
        boolean hasLock = false;
        if (CollectionUtils.isEmpty(logITaskLockPOList)) {
            hasLock = false;
        } else {
            List expireTaskLock;
            long current = System.currentTimeMillis() / 1000L;
            List noExpireTaskLock = logITaskLockPOList.stream().filter(logITaskLockPO -> logITaskLockPO.getCreateTime().getTime() / 1000L + logITaskLockPO.getExpireTime() >= current).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(noExpireTaskLock)) {
                for (LogITaskLockPO logITaskLockPO2 : noExpireTaskLock) {
                    if (!workerCode.equals(logITaskLockPO2.getWorkerCode())) continue;
                    hasLock = true;
                }
            }
            if (!CollectionUtils.isEmpty(expireTaskLock = logITaskLockPOList.stream().filter(logITaskLockPO -> logITaskLockPO.getCreateTime().getTime() / 1000L + logITaskLockPO.getExpireTime() < current).collect(Collectors.toList()))) {
                for (LogITaskLockPO logITaskLockPO3 : expireTaskLock) {
                    this.logITaskLockMapper.deleteByWorkerCodeAndAppName(logITaskLockPO3.getWorkerCode(), this.logIJobProperties.getAppName());
                }
            }
        }
        if (!hasLock) {
            LogITaskLockPO taskLock = new LogITaskLockPO();
            taskLock.setTaskCode(taskCode);
            taskLock.setWorkerCode(workerCode);
            taskLock.setExpireTime(expireTime);
            taskLock.setCreateTime(new Timestamp(System.currentTimeMillis()));
            taskLock.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            taskLock.setAppName(this.logIJobProperties.getAppName());
            try {
                return this.logITaskLockMapper.insert(taskLock) > 0;
            } catch (Exception e) {
                if (e.getMessage().contains("Duplicate")) {
                    logger.info("class=TaskLockServiceImpl||method=tryAcquire||taskCode={}||msg=duplicate key", (Object)taskCode);
                } else {
                    logger.error("class=TaskLockServiceImpl||method=tryAcquire||taskCode={}||msg={}", (Object)taskCode, (Object)e.getMessage());
                }
                return false;
            }
        }
        return hasLock;
    }

    @Override
    public Boolean tryRelease(String taskCode) {
        return this.tryRelease(taskCode, WorkerSingleton.getInstance().getLogIWorker().getWorkerCode());
    }

    @Override
    public Boolean tryRelease(String taskCode, String workerCode) {
        List<LogITaskLockPO> logITaskLockPOList = this.logITaskLockMapper.selectByTaskCodeAndWorkerCode(taskCode, workerCode, this.logIJobProperties.getAppName());
        if (CollectionUtils.isEmpty(logITaskLockPOList)) {
            logger.error("class=TaskLockServiceImpl||method=tryRelease||msg=taskCode={}, workerCode={}", (Object)taskCode, (Object)workerCode);
            return false;
        }
        long current = System.currentTimeMillis() / 1000L;
        List<Long> taskLockIdList = logITaskLockPOList.stream().filter(logITaskLockPO -> logITaskLockPO.getCreateTime().getTime() / 1000L + logITaskLockPO.getExpireTime() < current).map(LogITaskLockPO::getId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(taskLockIdList)) {
            return true;
        }
        int result = this.logITaskLockMapper.deleteByIds(taskLockIdList);
        return result > 0;
    }

    @Override
    public List<LogITaskLockVO> getAll() {
        List<LogITaskLockPO> logITaskLockPOS = this.logITaskLockMapper.selectByAppName(this.logIJobProperties.getAppName());
        if (CollectionUtils.isEmpty(logITaskLockPOS)) {
            return null;
        }
        return logITaskLockPOS.stream().map(logITaskLockPO -> BeanUtil.convertTo(logITaskLockPO, LogITaskLockVO.class)).collect(Collectors.toList());
    }

    @Override
    public void renewAll() {
    }
}

