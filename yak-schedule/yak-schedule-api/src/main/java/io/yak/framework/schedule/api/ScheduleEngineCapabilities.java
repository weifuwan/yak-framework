package io.yak.framework.schedule.api;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/** 调度引擎声明的能力，用于核心层在保存前进行显式校验。 */
public record ScheduleEngineCapabilities(
        Set<TriggerType> triggerTypes,
        Set<ConcurrencyPolicy> concurrencyPolicies,
        Set<MisfirePolicy> misfirePolicies,
        boolean pauseResume,
        boolean runNow,
        boolean timezone,
        boolean dynamicCreate) {

    public ScheduleEngineCapabilities {
        triggerTypes = immutableEnumSet(triggerTypes, TriggerType.class);
        concurrencyPolicies = immutableEnumSet(
                concurrencyPolicies, ConcurrencyPolicy.class);
        misfirePolicies = immutableEnumSet(
                misfirePolicies, MisfirePolicy.class);
    }

    public boolean supports(TriggerType type) {
        return triggerTypes.contains(type);
    }

    public boolean supports(ConcurrencyPolicy policy) {
        return concurrencyPolicies.contains(policy);
    }

    public boolean supports(MisfirePolicy policy) {
        return misfirePolicies.contains(policy);
    }

    private static <E extends Enum<E>> Set<E> immutableEnumSet(
            Set<E> values,
            Class<E> enumType) {
        if (values == null || values.isEmpty()) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(EnumSet.copyOf(values));
    }
}
