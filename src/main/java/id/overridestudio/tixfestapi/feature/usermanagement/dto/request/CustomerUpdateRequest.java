package id.overridestudio.tixfestapi.feature.usermanagement.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerUpdateRequest {
    private String firstName;
    private String lastName;
    private String dob;
    private String gender;
    private String phoneNumber;
}
