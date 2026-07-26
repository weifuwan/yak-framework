package io.yak.framework.schedule.model;

/** Defines what happens when the previous execution is still running. */
public enum ConcurrencyPolicy {
  ALLOW,
  FORBID
}
