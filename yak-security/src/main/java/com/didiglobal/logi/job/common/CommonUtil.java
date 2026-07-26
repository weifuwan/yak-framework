/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  org.springframework.util.StringUtils
 */
package com.didiglobal.logi.job.common;

import org.springframework.util.StringUtils;

public class CommonUtil {
    public static String sqlFuzzyQueryTransfer(String str) {
        if (!StringUtils.isEmpty((Object)str) && str.contains("%")) {
            str = str.replaceAll("%", "\\\\%");
        }
        if (!StringUtils.isEmpty((Object)str) && str.contains("_")) {
            str = str.replaceAll("_", "\\\\_");
        }
        return str;
    }

    public static boolean isCopyTask(String taskCode) {
        return taskCode.contains("-");
    }
}

