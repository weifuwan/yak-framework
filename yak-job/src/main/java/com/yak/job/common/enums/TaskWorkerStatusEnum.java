/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.job.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum TaskWorkerStatusEnum {
    WAITING(1),
    RUNNING(2),
    STOPPED(3);

    private static Map<Integer, TaskWorkerStatusEnum> map;

    static {
        map = new HashMap<Integer, TaskWorkerStatusEnum>(8);
        map.put(WAITING.getValue(), WAITING);
        map.put(RUNNING.getValue(), RUNNING);
        map.put(STOPPED.getValue(), STOPPED);
    }

    private Integer value;

    private TaskWorkerStatusEnum(Integer value) {
        this.value = value;
    }

    public static TaskWorkerStatusEnum get(Integer value) {
        return map.get(value);
    }

    public Integer getValue() {
        return this.value;
    }
}

