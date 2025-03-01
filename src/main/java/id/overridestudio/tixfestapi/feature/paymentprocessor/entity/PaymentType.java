package id.overridestudio.tixfestapi.feature.paymentprocessor.entity;

import lombok.Getter;

@Getter
public enum PaymentType {
    BANK_TRANSFER("bank_transfer"),
    GOPAY("gopay"),
    SHOPEEPAY("shopeepay"),
    QRIS("qris"),
    CSTORE("cstore");

    private final String description;

    PaymentType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static PaymentType findByName(String desc) {
        for (PaymentType paymentType : PaymentType.values()) {
            if (paymentType.getDescription().equalsIgnoreCase(desc)) {
                return paymentType;
            }
        }
        return null;
    }
}
