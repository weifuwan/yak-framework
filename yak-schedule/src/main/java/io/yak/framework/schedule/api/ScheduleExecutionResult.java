package io.yak.framework.schedule.api;

/** Business-side acknowledgement returned to the scheduling layer. */
public final class ScheduleExecutionResult {

    private final boolean accepted;
    private final String businessExecutionId;
    private final String message;

    private ScheduleExecutionResult(
            boolean accepted,
            String businessExecutionId,
            String message) {

        this.accepted = accepted;
        this.businessExecutionId = businessExecutionId;
        this.message = message;
    }

    public static ScheduleExecutionResult accepted(
            String businessExecutionId) {

        return new ScheduleExecutionResult(
                true,
                businessExecutionId,
                null);
    }

    public static ScheduleExecutionResult accepted(
            String businessExecutionId,
            String message) {

        return new ScheduleExecutionResult(
                true,
                businessExecutionId,
                message);
    }

    public static ScheduleExecutionResult rejected(String message) {
        return new ScheduleExecutionResult(false, null, message);
    }

    public boolean isAccepted() {
        return accepted;
    }

    public String getBusinessExecutionId() {
        return businessExecutionId;
    }

    public String getMessage() {
        return message;
    }
}
