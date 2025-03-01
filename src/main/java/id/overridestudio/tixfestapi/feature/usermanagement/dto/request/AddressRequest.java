package id.overridestudio.tixfestapi.feature.usermanagement.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressRequest {
    private String street;
    private String city;
    private String province;
    private String postalCode;
}
