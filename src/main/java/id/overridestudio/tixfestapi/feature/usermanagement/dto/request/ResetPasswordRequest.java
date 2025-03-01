package id.overridestudio.tixfestapi.feature.usermanagement.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResetPasswordRequest {
    private String email;
    private String otp;
    private String password;
}
