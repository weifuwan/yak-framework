/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 *
 * Could not load the following classes:
 *  javax.annotation.PostConstruct
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.BeansException
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.config.BeanPostProcessor
 *  org.springframework.stereotype.Component
 */
package com.yak.job;

import com.yak.job.annotation.Task;
import com.yak.job.common.enums.TaskStatusEnum;
import com.yak.job.common.po.YakTaskPO;
import com.yak.job.core.job.Job;
import com.yak.job.core.job.JobFactory;
import com.yak.job.mapper.YakTaskMapper;
import com.yak.job.utils.CronExpression;
import com.yak.job.utils.IdWorker;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class TaskBeanPostProcessor
        implements BeanPostProcessor {
    private static final Logger logger = LoggerFactory.getLogger(TaskBeanPostProcessor.class);
    private static Map<String, YakTaskPO> taskMap = new HashMap<String, YakTaskPO>();
    @Autowired
    private YakTaskMapper yakTaskMapper;
    @Autowired
    private JobFactory jobFactory;
    @Autowired
    private YakJobProperties yakJobProperties;

    @PostConstruct
    public void init() {
        logger.info("class=TaskBeanPostProcessor||method=init");
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        try {
            if (!this.yakJobProperties.getEnable().booleanValue()) {
                return bean;
            }
            Class<?> beanClass = bean.getClass();
            if (!(bean instanceof Job)) {
                return bean;
            }
            this.jobFactory.addJob(beanClass.getCanonicalName(), (Job) bean);
            Task taskAnnotation = beanClass.getAnnotation(Task.class);
            if (taskAnnotation == null || !taskAnnotation.autoRegister()) {
                return bean;
            }
            if (!this.check(taskAnnotation)) {
                logger.error("class=TaskBeanPostProcessor||method=blacklist||url=||msg=invalid schedule {}", (Object) taskAnnotation.toString());
            }
            if (!this.contains(beanClass.getCanonicalName())) {
                YakTaskPO task = this.getNewLogTask(beanClass, taskAnnotation);
                task.setTaskCode(IdWorker.getIdStr());
                task.setStatus(TaskStatusEnum.RUNNING.getValue());
                this.yakTaskMapper.insert(task);
            } else {
                YakTaskPO task = taskMap.get(beanClass.getCanonicalName());
                task = this.updateLogTask(task, beanClass, taskAnnotation);
                this.yakTaskMapper.updateByCode(task);
            }
        } catch (Exception e) {
            logger.error("class=TaskBeanPostProcessor||method=postProcessAfterInitialization||beanName={}||msg=exception", (Object) beanName, (Object) e);
        }
        return bean;
    }

    private boolean check(Task schedule) {
        return CronExpression.isValidExpression(schedule.cron());
    }

    private YakTaskPO getNewLogTask(Class<?> beanClass, Task schedule) {
        YakTaskPO yakTaskPO = new YakTaskPO();
        yakTaskPO.setTaskName(schedule.name());
        yakTaskPO.setTaskDesc(schedule.description());
        yakTaskPO.setCron(schedule.cron());
        yakTaskPO.setClassName(beanClass.getCanonicalName());
        yakTaskPO.setParams("");
        yakTaskPO.setRetryTimes(schedule.retryTimes());
        yakTaskPO.setLastFireTime(new Timestamp(System.currentTimeMillis()));
        yakTaskPO.setTimeout(schedule.timeout());
        yakTaskPO.setSubTaskCodes("");
        yakTaskPO.setConsensual(schedule.consensual().name());
        yakTaskPO.setTaskWorkerStr("");
        yakTaskPO.setAppName(this.yakJobProperties.getAppName());
        yakTaskPO.setOwner(schedule.owner());
        return yakTaskPO;
    }

    private YakTaskPO updateLogTask(YakTaskPO yakTaskPO, Class<?> beanClass, Task schedule) {
        yakTaskPO.setTaskName(schedule.name());
        yakTaskPO.setTaskDesc(schedule.description());
        yakTaskPO.setCron(schedule.cron());
        yakTaskPO.setClassName(beanClass.getCanonicalName());
        yakTaskPO.setParams("");
        yakTaskPO.setRetryTimes(schedule.retryTimes());
        yakTaskPO.setTimeout(schedule.timeout());
        yakTaskPO.setConsensual(schedule.consensual().name());
        yakTaskPO.setAppName(this.yakJobProperties.getAppName());
        yakTaskPO.setOwner(schedule.owner());
        return yakTaskPO;
    }

    private boolean contains(String className) {
        if (taskMap.isEmpty()) {
            List<YakTaskPO> yakTaskPOS = this.yakTaskMapper.selectByAppName(this.yakJobProperties.getAppName());
            taskMap = yakTaskPOS.stream().collect(Collectors.toMap(YakTaskPO::getClassName, Function.identity()));
        }
        return taskMap.containsKey(className);
    }
}

