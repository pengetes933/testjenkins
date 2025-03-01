package id.overridestudio.tixfestapi.feature.auth.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {
    private String id;
    private String accessToken;
    private Long expiresIn;
    private String refreshToken;
    private Integer refreshExpiresIn;
    private String role;
}