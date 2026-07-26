/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.job.common.enums;

public enum TaskStatusEnum {
    STOP(0),
    RUNNING(1);

    private Integer value;

    private TaskStatusEnum(Integer value) {
        this.value = value;
    }

    public static boolean isValid(Integer status) {
        for (TaskStatusEnum taskStatusEnum : TaskStatusEnum.values()) {
            if (status.intValue() != taskStatusEnum.value.intValue()) continue;
            return true;
        }
        return false;
    }

    public Integer getValue() {
        return this.value;
    }
}

