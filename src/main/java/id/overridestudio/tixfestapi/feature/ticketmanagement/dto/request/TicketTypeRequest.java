package id.overridestudio.tixfestapi.feature.ticketmanagement.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketTypeRequest {

    private String ticketId;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean isOpen;
}
