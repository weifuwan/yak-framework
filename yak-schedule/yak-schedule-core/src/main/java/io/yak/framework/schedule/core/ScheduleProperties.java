package io.yak.framework.schedule.core;

import io.yak.framework.schedule.api.ScheduleEngineTypes;
import org.springframework.boot.context.properties.ConfigurationProperties;

/** Yak Schedule 核心配置。 */
@ConfigurationProperties("yak.schedule")
public class ScheduleProperties {
    private boolean enabled = true;
    private String engine = ScheduleEngineTypes.QUARTZ;
    private int inMemoryCapacity = 10_000;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public int getInMemoryCapacity() {
        return inMemoryCapacity;
    }

    public void setInMemoryCapacity(int inMemoryCapacity) {
        this.inMemoryCapacity = inMemoryCapacity;
    }
}
