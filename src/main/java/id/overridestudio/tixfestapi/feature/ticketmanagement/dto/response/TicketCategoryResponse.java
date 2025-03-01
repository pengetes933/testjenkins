package id.overridestudio.tixfestapi.feature.ticketmanagement.dto.response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketCategoryResponse {

    private String id;
    private String name;
    private String description;
}
