package id.overridestudio.tixfestapi.feature.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequest {
    @NotBlank(message = "Credential is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}
