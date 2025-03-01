package id.overridestudio.tixfestapi.feature.eventmanagement.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventRequest {

    private String name;
    private String excerpt;
    private String description;
    private String city;
    private LocalDateTime date;

    //TODO: Banner, PosterSquare, PosterRectangle
}
