package id.overridestudio.tixfestapi.feature.paymentprocessor.dto.response;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PaymentResponse {

    private String statusCode;
    private String statusMessage;
    private String orderId;
    private String amount;
    private String paymentStatus;
    private String paymentType;
    private String paymentTime;
    private String fraudStatus;
    private List<EWalletActionsResponse> walletActions;
    private VANumbersResponse bankTransfer;
    private String storePaymentCode;
    private String expiryTime;

}
