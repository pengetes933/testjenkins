package id.overridestudio.tixfestapi.feature.usermanagement.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventOrganizerResponse {
    private String id;
    private String name;
    private String description;
    private Boolean isVerified;
    private String eventOrganizerAddressId;
    private String bankAccountId;
    private Integer totalFollower;
    private FileResponse profilePicture;
}
