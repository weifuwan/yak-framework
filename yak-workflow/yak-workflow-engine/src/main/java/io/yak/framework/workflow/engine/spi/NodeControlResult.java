package io.yak.framework.workflow.engine.spi;

/** Result of requesting an optional executor-side lifecycle control operation. */
public enum NodeControlResult {
    ACCEPTED,
    UNSUPPORTED
}
