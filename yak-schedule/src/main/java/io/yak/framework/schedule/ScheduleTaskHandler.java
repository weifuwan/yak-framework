package io.yak.framework.schedule;

import java.util.Map;

/** Application extension point. Bean names are referenced by task definitions. */
@FunctionalInterface
public interface ScheduleTaskHandler {
  void execute(Map<String, String> parameters) throws Exception;
}
