/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.job.utils;

public interface IdentifierGenerator {
    public Number nextId(Object var1);

    default public String nextUuid(Object entity) {
        return IdWorker.get32Uuid();
    }
}

