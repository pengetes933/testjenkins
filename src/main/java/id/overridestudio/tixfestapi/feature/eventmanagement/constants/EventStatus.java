package id.overridestudio.tixfestapi.feature.eventmanagement.constants;

public enum EventStatus {
    DRAFT("Draft"),
    SUBMITTED("Submitted"),
    APPROVED("Approved"),
    UPCOMING("Upcoming"),
    OPEN("Open"),
    CLOSED("Closed"),
    CANCELED("Canceled"),
    FINISHED("Finished");

    private final String description;

    EventStatus(String description) {
        this.description = description;
    }

    public static EventStatus findByDesc(String desc) {
        for (EventStatus value : values()) {
            if (value.description.equalsIgnoreCase(desc)) {
                return value;
            }
        }
        return null;
    }

}
