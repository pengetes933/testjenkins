package id.overridestudio.tixfestapi.feature.usermanagement.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventOrganizerUpdateRequest {
    private String name;
    private String description;
}
