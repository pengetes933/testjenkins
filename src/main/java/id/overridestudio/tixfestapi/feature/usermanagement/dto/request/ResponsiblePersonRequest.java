package id.overridestudio.tixfestapi.feature.usermanagement.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponsiblePersonRequest {
    private String name;
    private String nik;
    private String phoneNumber;
    private String street;
    private String city;
    private String province;
    private String postalCode;
}
