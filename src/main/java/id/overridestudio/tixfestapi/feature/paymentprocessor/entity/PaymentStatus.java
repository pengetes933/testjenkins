package id.overridestudio.tixfestapi.feature.paymentprocessor.entity;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    AUTHORIZE("authorize"),
    CAPTURE("capture"),
    SETTLEMENT("settlement"),
    PENDING("pending"),
    DENY("deny"),
    CANCEL("cancel"),
    REFUND("refund"),
    EXPIRE("expire"),
    FAILURE("failure");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static PaymentStatus findByName(String desc) {
        for (PaymentStatus paymentStatus : PaymentStatus.values()) {
            if (paymentStatus.getDescription().equalsIgnoreCase(desc)) {
                return paymentStatus;
            }
        }
        return null;
    }

}
