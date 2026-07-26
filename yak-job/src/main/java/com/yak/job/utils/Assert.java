/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.job.utils;

public class Assert {
    public static void notNull(Object obj, String msg) {
        if (obj == null) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static void isTrue(boolean test, String msg) {
        if (!test) {
            throw new IllegalStateException(msg);
        }
    }

    public static void isFalse(boolean test, String message) {
        Assert.isTrue(!test, message);
    }
}

