package id.overridestudio.tixfestapi.feature.usermanagement.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponsiblePersonResponse {
    private String id;
    private String name;
    private String nik;
    private String phoneNumber;
    private String street;
    private String city;
    private String province;
    private String postalCode;
    private String status;
    private Boolean isVerified;
}