package io.yak.framework.schedule.provider.quartz;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.quartz.JobDataMap;

/** Internal Quartz data contract kept stable across provider upgrades. */
final class QuartzScheduleData {

    static final String NAMESPACE = "schedule.namespace";
    static final String NAME = "schedule.name";
    static final String DISPLAY_NAME = "schedule.displayName";
    static final String DESCRIPTION = "schedule.description";
    static final String HANDLER = "schedule.handler";
    static final String CONCURRENCY = "schedule.concurrency";
    static final String MISFIRE = "schedule.misfire";
    static final String MAX_TRIGGER_RETRIES =
            "schedule.maxTriggerRetries";
    static final String VERSION = "schedule.version";
    static final String OPERATOR = "schedule.operator";
    static final String MANUAL = "schedule.manual";
    static final String TRIGGER_ID = "schedule.triggerId";
    static final String PAYLOAD_PREFIX = "payload.";
    static final String METADATA_PREFIX = "metadata.";

    private QuartzScheduleData() {
    }

    static Map<String, String> values(
            JobDataMap data,
            String prefix) {

        Map<String, String> result = new LinkedHashMap<>();

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = entry.getKey();
            if (key == null || !key.startsWith(prefix)) {
                continue;
            }
            result.put(
                    key.substring(prefix.length()),
                    String.valueOf(entry.getValue()));
        }

        if (result.isEmpty()) {
            return Collections.emptyMap();
        }
        return Collections.unmodifiableMap(result);
    }
}
