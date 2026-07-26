/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 *
 * Could not load the following classes:
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package com.yak.job.utils;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadUtil {
    private static final Logger logger = LoggerFactory.getLogger(ThreadUtil.class);

    public static void sleep(long time, TimeUnit timeUnit) {
        try {
            switch (timeUnit) {
                case DAYS: {
                    TimeUnit.DAYS.sleep(time);
                    break;
                }
                case HOURS: {
                    TimeUnit.HOURS.sleep(time);
                    break;
                }
                case MINUTES: {
                    TimeUnit.MINUTES.sleep(time);
                    break;
                }
                case SECONDS: {
                    TimeUnit.SECONDS.sleep(time);
                    break;
                }
                case NANOSECONDS: {
                    TimeUnit.NANOSECONDS.sleep(time);
                    break;
                }
                case MICROSECONDS: {
                    TimeUnit.MICROSECONDS.sleep(time);
                    break;
                }
                case MILLISECONDS: {
                    TimeUnit.MILLISECONDS.sleep(time);
                    break;
                }
            }
        } catch (InterruptedException e) {
            logger.error("class=ThreadUtil||method=sleep||url=||msg={}", (Throwable) e);
        }
    }
}

