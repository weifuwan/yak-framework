/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 *
 * Could not load the following classes:
 *  org.springframework.beans.BeansException
 *  org.springframework.context.ApplicationContext
 *  org.springframework.context.ApplicationContextAware
 *  org.springframework.stereotype.Component
 */
package com.yak.job.core.consensual;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ConsensualFactory
        implements ApplicationContextAware {
    private static Map<String, Consensual> consensualMap = new HashMap<String, Consensual>();
    private ApplicationContext applicationContext;

    public Consensual getConsensual(String name) {
        this.init();
        Consensual consensual = consensualMap.get(name);
        if (consensual == null) {
            throw new IllegalArgumentException("no such consensual " + name);
        }
        return consensual;
    }

    private void init() {
        if (!consensualMap.isEmpty()) {
            return;
        }
        Map beans = this.applicationContext.getBeansOfType(Consensual.class);
        for (Consensual consensual : beans.values()) {
            consensualMap.put(consensual.getName(), consensual);
        }
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

