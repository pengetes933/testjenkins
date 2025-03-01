package id.overridestudio.tixfestapi.feature.auth.service;

import id.overridestudio.tixfestapi.feature.usermanagement.entity.UserAccount;
import jakarta.servlet.http.HttpServletRequest;

public interface JwtService {
    String generateAccessToken(UserAccount userAccount, Long accessTokenExpiry);
    String getUserId(String token);
    String extractTokenFromRequest(HttpServletRequest request);
    void blacklistAccessToken(String bearerToken);
    boolean isTokenBlacklisted(String token);
}
