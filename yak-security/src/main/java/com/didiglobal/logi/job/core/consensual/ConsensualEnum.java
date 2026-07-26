/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.didiglobal.logi.job.core.consensual;

import java.util.HashMap;
import java.util.Map;

public enum ConsensualEnum {
    RANDOM("\u968f\u673a\u62a2\u5360"),
    BROADCAST("\u5e7f\u64ad");

    private static Map<String, ConsensualEnum> map;
    private String desc;

    private ConsensualEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return this.desc;
    }

    public static ConsensualEnum getByName(String name) {
        return map.get(name);
    }

    static {
        map = new HashMap<String, ConsensualEnum>(8);
        map.put(RANDOM.name(), RANDOM);
        map.put(BROADCAST.name(), BROADCAST);
    }
}

