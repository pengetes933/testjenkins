package id.overridestudio.tixfestapi.feature.transactionprocessing.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderDetailResponse {

    private String id;
    private String ticketId;
    private String ticketName;
    private String quantity;
    private String subtotal;

}
