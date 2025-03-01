package id.overridestudio.tixfestapi.feature.usermanagement.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordRequest {
    private String currentPassword;
    private String newPassword;
}
