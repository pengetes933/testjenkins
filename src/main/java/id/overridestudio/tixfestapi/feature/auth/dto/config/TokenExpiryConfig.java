package id.overridestudio.tixfestapi.feature.auth.dto.config;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenExpiryConfig {
    private Long accessTokenExpiry;
    private Integer refreshTokenExpiry;
}
