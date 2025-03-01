package id.overridestudio.tixfestapi.feature.ticketmanagement.dto.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketCategoryRequest {

    private String ticketId;
    private String name;
    private String description;
}
