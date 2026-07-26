package io.yak.framework.schedule.model;

import java.time.ZoneId;
import java.util.Map;

/** Complete, project-scoped definition of a scheduled task. */
public record ScheduleTaskDefinition(
    String project,
    String name,
    String description,
    String handler,
    String cron,
    ZoneId zoneId,
    ConcurrencyPolicy concurrencyPolicy,
    int maxRetries,
    Map<String, String> parameters) {

  public ScheduleTaskDefinition {
    if (project == null || project.isBlank()) throw new IllegalArgumentException("project must not be blank");
    if (name == null || name.isBlank()) throw new IllegalArgumentException("name must not be blank");
    if (handler == null || handler.isBlank()) throw new IllegalArgumentException("handler must not be blank");
    if (cron == null || cron.isBlank()) throw new IllegalArgumentException("cron must not be blank");
    if (maxRetries < 0) throw new IllegalArgumentException("maxRetries must not be negative");
    zoneId = zoneId == null ? ZoneId.systemDefault() : zoneId;
    concurrencyPolicy = concurrencyPolicy == null ? ConcurrencyPolicy.FORBID : concurrencyPolicy;
    parameters = parameters == null ? Map.of() : Map.copyOf(parameters);
  }
}
