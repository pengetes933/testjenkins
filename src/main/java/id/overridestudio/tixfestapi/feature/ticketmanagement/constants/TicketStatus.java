package id.overridestudio.tixfestapi.feature.ticketmanagement.constants;

public enum TicketStatus {
    OPEN("Open"),
    SOLD("Sold");

    private final String description;

    TicketStatus(String description) {
        this.description = description;
    }

    public static TicketStatus findByDesc(String desc) {
        for (TicketStatus value : values()) {
            if (value.description.equalsIgnoreCase(desc)) {
                return value;
            }
        }
        return null;
    }

}
