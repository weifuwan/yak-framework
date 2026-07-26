/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Component
 */
package com.yak.job.core.job;

import com.yak.job.YakJobProperties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JobExecutor {
    public ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    public JobExecutor(YakJobProperties properties) {
        this.threadPoolExecutor = new ThreadPoolExecutor(properties.getInitThreadNum(), properties.getMaxThreadNum(), 10L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(100));
    }

    public <T> Future<T> submit(Callable<T> task) {
        return this.threadPoolExecutor.submit(task);
    }
}

