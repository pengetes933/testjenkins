package id.overridestudio.tixfestapi.feature.auth.service;

public interface RefreshTokenService {
    String createToken(String userId, Integer refreshTokenExpiry);
    void deleteRefreshToken(String userId);
    String rotateRefreshToken(String userId, Integer refreshTokenExpiry);
    String getUserIdByToken(String token);
}
