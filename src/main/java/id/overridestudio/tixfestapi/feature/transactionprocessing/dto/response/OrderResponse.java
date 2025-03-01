package id.overridestudio.tixfestapi.feature.transactionprocessing.dto.response;

import id.overridestudio.tixfestapi.feature.transactionprocessing.entity.OrderStatus;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderResponse {

    private String id;
    private String customerId;
    private String customerName;
    private List<OrderDetailResponse> orderDetails;
    private String totalPrice;
    private String status;
    private String orderDate;

}
