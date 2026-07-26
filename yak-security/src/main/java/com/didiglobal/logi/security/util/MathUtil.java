/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.didiglobal.logi.security.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MathUtil {
    public static long getRandomNumber(int len) {
        if (len <= 0 || len > 18) {
            return 0L;
        }
        return (long)((Math.random() + 1.0) * Math.pow(10.0, len));
    }

    public static Set<Integer> getIntersection(List<Integer> list1, List<Integer> list2) {
        HashSet<Integer> result = new HashSet<Integer>();
        HashSet<Integer> set = new HashSet<Integer>(list2);
        for (Integer num : list1) {
            if (!set.contains(num)) continue;
            result.add(num);
        }
        return result;
    }

    private MathUtil() {
    }
}

