/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  org.springframework.stereotype.Component
 */
package com.didiglobal.logi.job.annotation;

import com.didiglobal.logi.job.core.consensual.ConsensualEnum;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.stereotype.Component;

@Target(value={ElementType.TYPE})
@Retention(value=RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Task {
    public String name() default "";

    public String description() default "";

    public String owner() default "system";

    public String cron() default "";

    public int retryTimes() default 0;

    public long timeout() default 0L;

    public boolean autoRegister() default false;

    public ConsensualEnum consensual() default ConsensualEnum.RANDOM;
}

