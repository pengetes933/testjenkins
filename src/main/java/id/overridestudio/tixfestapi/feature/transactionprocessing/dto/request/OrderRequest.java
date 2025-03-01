package id.overridestudio.tixfestapi.feature.transactionprocessing.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderRequest {

    @NotEmpty(message = "order detail cannot be empty")
    private List<OrderDetailRequest> orderDetails;
}
