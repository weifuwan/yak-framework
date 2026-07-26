/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.job.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum JobStatusEnum {
    STARTED(0),
    RUNNING(1),
    SUCCEED(2),
    FAILED(3),
    CANCELED(4),
    TIMEOUT(5);

    private static Map<Integer, JobStatusEnum> map;
    private Integer value;

    public Integer getValue() {
        return this.value;
    }

    private JobStatusEnum(Integer value) {
        this.value = value;
    }

    public static JobStatusEnum get(Integer value) {
        return map.get(value);
    }

    static {
        map = new HashMap<Integer, JobStatusEnum>(8);
        map.put(STARTED.getValue(), STARTED);
        map.put(SUCCEED.getValue(), SUCCEED);
        map.put(FAILED.getValue(), FAILED);
        map.put(CANCELED.getValue(), CANCELED);
        map.put(TIMEOUT.getValue(), TIMEOUT);
    }
}

