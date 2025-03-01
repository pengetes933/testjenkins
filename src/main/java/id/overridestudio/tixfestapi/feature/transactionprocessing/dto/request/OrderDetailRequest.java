package id.overridestudio.tixfestapi.feature.transactionprocessing.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderDetailRequest {

    @NotNull(message = "ticket id is required")
    private String ticketId;

    @NotNull(message = "quantity is required")
    private Integer quantity;
}
