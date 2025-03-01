package id.overridestudio.tixfestapi.feature.eventmanagement.dto.response;

import id.overridestudio.tixfestapi.feature.eventmanagement.constants.EventStatus;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventResponse {

    private String id;
    private String eventOrganizerId;
    private String name;
    private String excerpt;
    private String description;
    private String city;
    private LocalDateTime date;
    private EventStatus eventStatus;
    private Integer sumRating;
    private Integer countRating;
    private Long avgRating;
}
