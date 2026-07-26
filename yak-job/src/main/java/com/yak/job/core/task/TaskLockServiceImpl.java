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
package com.yak.job.core.task;

import com.yak.job.YakJobProperties;
import com.yak.job.common.po.YakTaskLockPO;
import com.yak.job.common.vo.YakTaskLockVO;
import com.yak.job.core.WorkerSingleton;
import com.yak.job.mapper.YakTaskLockMapper;
import com.yak.job.utils.BeanUtil;
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
    private YakTaskLockMapper yakTaskLockMapper;
    private YakJobProperties yakJobProperties;

    @Autowired
    public TaskLockServiceImpl(YakTaskLockMapper yakTaskLockMapper, YakJobProperties yakJobProperties) {
        this.yakTaskLockMapper = yakTaskLockMapper;
        this.yakJobProperties = yakJobProperties;
    }

    @Override
    public Boolean tryAcquire(String taskCode) {
        return this.tryAcquire(taskCode, WorkerSingleton.getInstance().getYakWorker().getWorkerCode(), EXPIRE_TIME_SECONDS);
    }

    @Override
    public Boolean tryAcquire(String taskCode, String workerCode, Long expireTime) {
        List<YakTaskLockPO> yakTaskLockPOList = this.yakTaskLockMapper.selectByTaskCode(taskCode, this.yakJobProperties.getAppName());
        boolean hasLock = false;
        if (CollectionUtils.isEmpty(yakTaskLockPOList)) {
            hasLock = false;
        } else {
            List expireTaskLock;
            long current = System.currentTimeMillis() / 1000L;
            List noExpireTaskLock = yakTaskLockPOList.stream().filter(yakTaskLockPO -> yakTaskLockPO.getCreateTime().getTime() / 1000L + yakTaskLockPO.getExpireTime() >= current).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(noExpireTaskLock)) {
                for (YakTaskLockPO yakTaskLockPO2 : noExpireTaskLock) {
                    if (!workerCode.equals(yakTaskLockPO2.getWorkerCode())) continue;
                    hasLock = true;
                }
            }
            if (!CollectionUtils.isEmpty(expireTaskLock = yakTaskLockPOList.stream().filter(yakTaskLockPO -> yakTaskLockPO.getCreateTime().getTime() / 1000L + yakTaskLockPO.getExpireTime() < current).collect(Collectors.toList()))) {
                for (YakTaskLockPO yakTaskLockPO3 : expireTaskLock) {
                    this.yakTaskLockMapper.deleteByWorkerCodeAndAppName(yakTaskLockPO3.getWorkerCode(), this.yakJobProperties.getAppName());
                }
            }
        }
        if (!hasLock) {
            YakTaskLockPO taskLock = new YakTaskLockPO();
            taskLock.setTaskCode(taskCode);
            taskLock.setWorkerCode(workerCode);
            taskLock.setExpireTime(expireTime);
            taskLock.setCreateTime(new Timestamp(System.currentTimeMillis()));
            taskLock.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            taskLock.setAppName(this.yakJobProperties.getAppName());
            try {
                return this.yakTaskLockMapper.insert(taskLock) > 0;
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
        return this.tryRelease(taskCode, WorkerSingleton.getInstance().getYakWorker().getWorkerCode());
    }

    @Override
    public Boolean tryRelease(String taskCode, String workerCode) {
        List<YakTaskLockPO> yakTaskLockPOList = this.yakTaskLockMapper.selectByTaskCodeAndWorkerCode(taskCode, workerCode, this.yakJobProperties.getAppName());
        if (CollectionUtils.isEmpty(yakTaskLockPOList)) {
            logger.error("class=TaskLockServiceImpl||method=tryRelease||msg=taskCode={}, workerCode={}", (Object)taskCode, (Object)workerCode);
            return false;
        }
        long current = System.currentTimeMillis() / 1000L;
        List<Long> taskLockIdList = yakTaskLockPOList.stream().filter(yakTaskLockPO -> yakTaskLockPO.getCreateTime().getTime() / 1000L + yakTaskLockPO.getExpireTime() < current).map(YakTaskLockPO::getId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(taskLockIdList)) {
            return true;
        }
        int result = this.yakTaskLockMapper.deleteByIds(taskLockIdList);
        return result > 0;
    }

    @Override
    public List<YakTaskLockVO> getAll() {
        List<YakTaskLockPO> yakTaskLockPOS = this.yakTaskLockMapper.selectByAppName(this.yakJobProperties.getAppName());
        if (CollectionUtils.isEmpty(yakTaskLockPOS)) {
            return null;
        }
        return yakTaskLockPOS.stream().map(yakTaskLockPO -> BeanUtil.convertTo(yakTaskLockPO, YakTaskLockVO.class)).collect(Collectors.toList());
    }

    @Override
    public void renewAll() {
    }
}

