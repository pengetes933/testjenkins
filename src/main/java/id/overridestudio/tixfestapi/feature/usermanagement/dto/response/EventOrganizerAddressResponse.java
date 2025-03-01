package id.overridestudio.tixfestapi.feature.usermanagement.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventOrganizerAddressResponse {
    private String id;
    private String street;
    private String city;
    private String province;
    private String postalCode;
}
