package io.yak.framework.schedule.api.model;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/** Explicit provider capability declaration. */
public final class ScheduleEngineCapabilities {

    private final Set<TriggerType> triggerTypes;
    private final boolean pauseResume;
    private final boolean runNow;
    private final boolean timezone;
    private final boolean distributedExecution;

    public ScheduleEngineCapabilities(
            Set<TriggerType> triggerTypes,
            boolean pauseResume,
            boolean runNow,
            boolean timezone,
            boolean distributedExecution) {

        if (triggerTypes == null || triggerTypes.isEmpty()) {
            this.triggerTypes = Collections.emptySet();
        } else {
            this.triggerTypes = Collections.unmodifiableSet(
                    EnumSet.copyOf(triggerTypes));
        }
        this.pauseResume = pauseResume;
        this.runNow = runNow;
        this.timezone = timezone;
        this.distributedExecution = distributedExecution;
    }

    public boolean supports(TriggerType triggerType) {
        return triggerTypes.contains(triggerType);
    }

    public Set<TriggerType> getTriggerTypes() {
        return triggerTypes;
    }

    public boolean isPauseResume() {
        return pauseResume;
    }

    public boolean isRunNow() {
        return runNow;
    }

    public boolean isTimezone() {
        return timezone;
    }

    public boolean isDistributedExecution() {
        return distributedExecution;
    }
}
