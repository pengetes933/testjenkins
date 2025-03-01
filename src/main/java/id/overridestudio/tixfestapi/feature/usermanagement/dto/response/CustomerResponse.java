package id.overridestudio.tixfestapi.feature.usermanagement.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponse {
    private String id;
    private String userAccountId;
    private String firstName;
    private String lastName;
    private String dob;
    private String gender;
    private String phoneNumber;
}
