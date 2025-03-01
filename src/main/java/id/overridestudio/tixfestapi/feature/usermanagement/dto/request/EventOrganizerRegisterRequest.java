package id.overridestudio.tixfestapi.feature.usermanagement.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventOrganizerRegisterRequest {
    private String email;
    private String password;
    private String name;
    private String description;
}
