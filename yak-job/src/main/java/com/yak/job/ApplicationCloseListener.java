/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 *
 * Could not load the following classes:
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.context.ApplicationContext
 *  org.springframework.context.ApplicationListener
 *  org.springframework.context.event.ContextClosedEvent
 *  org.springframework.stereotype.Service
 */
package com.yak.job;

import com.yak.job.core.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Service;

@Service
public class ApplicationCloseListener
        implements ApplicationListener<ContextClosedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationCloseListener.class);
    private ApplicationContext applicationContext;

    @Autowired
    public ApplicationCloseListener(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void onApplicationEvent(ContextClosedEvent event) {
        logger.error("class=ApplicationCloseListener||method=onApplicationEvent||url=||msg=shutdown auv job!!!");
        Scheduler scheduler = (Scheduler) this.applicationContext.getBean(Scheduler.class);
        scheduler.shutdown();
    }
}

