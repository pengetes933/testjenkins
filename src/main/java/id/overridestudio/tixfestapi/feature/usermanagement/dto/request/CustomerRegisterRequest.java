package id.overridestudio.tixfestapi.feature.usermanagement.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerRegisterRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String dob;
    private String gender;
    private String phoneNumber;
}
