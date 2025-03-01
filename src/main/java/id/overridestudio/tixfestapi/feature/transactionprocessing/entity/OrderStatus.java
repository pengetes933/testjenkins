package id.overridestudio.tixfestapi.feature.transactionprocessing.entity;

public enum OrderStatus {
    DRAFT("draft"),
    PENDING("pending"),    // order made, but not paid for
    PAID("paid"),   // order has been paid
    CONFIRMED("confirmed"), // payment has been verified and tickets confirmed
    EXPIRED("expired"),
    CANCELLED("canceled"),
    REFUNDED("refunded");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static OrderStatus findByName(String desc) {
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.getDescription().equalsIgnoreCase(desc)) {
                return orderStatus;
            }
        }
        return null;
    }
}
