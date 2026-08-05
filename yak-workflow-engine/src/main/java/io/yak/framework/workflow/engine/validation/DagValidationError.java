package io.yak.framework.workflow.engine.validation;

import java.util.Objects;

/**
 * One structural problem found in a workflow DAG.
 *
 * @param code machine-readable error code
 * @param message human-readable explanation
 * @param elementId related node code or edge identifier, when available
 */
public record DagValidationError(
        DagValidationErrorCode code, String message, String elementId) {

    public DagValidationError {
        Objects.requireNonNull(code, "code must not be null");
        Objects.requireNonNull(message, "message must not be null");
    }
}
