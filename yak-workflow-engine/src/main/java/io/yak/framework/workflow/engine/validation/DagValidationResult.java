package io.yak.framework.workflow.engine.validation;

import java.util.List;
import java.util.Objects;

/**
 * Result of validating one workflow definition.
 *
 * @param errors all detected validation errors
 */
public record DagValidationResult(List<DagValidationError> errors) {

    public DagValidationResult {
        errors = List.copyOf(Objects.requireNonNull(errors, "errors must not be null"));
    }

    public boolean isValid() {
        return errors.isEmpty();
    }

    public boolean hasError(DagValidationErrorCode code) {
        return errors.stream().anyMatch(error -> error.code() == code);
    }
}
