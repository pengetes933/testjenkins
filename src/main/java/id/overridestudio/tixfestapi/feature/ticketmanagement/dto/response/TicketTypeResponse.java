package id.overridestudio.tixfestapi.feature.ticketmanagement.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketTypeResponse {

    private String id;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean IsOpen;
}
