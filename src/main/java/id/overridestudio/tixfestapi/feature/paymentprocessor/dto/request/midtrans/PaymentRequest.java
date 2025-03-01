package id.overridestudio.tixfestapi.feature.paymentprocessor.dto.request.midtrans;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PaymentRequest {

    private String orderId;

    private String paymentType;

    private String bank;

    private String store;

}
