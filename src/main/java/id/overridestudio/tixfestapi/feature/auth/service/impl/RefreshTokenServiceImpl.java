package id.overridestudio.tixfestapi.feature.auth.service.impl;

import id.overridestudio.tixfestapi.core.service.RedisService;
import id.overridestudio.tixfestapi.feature.auth.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RedisService redisService;

    @Override
    public String createToken(String userId, Integer refreshTokenExpiry) {
        String refreshToken = UUID.randomUUID().toString();

        if (redisService.isExists("refreshToken:" + userId)) {
            deleteRefreshToken(userId);
        }

        redisService.save("refreshToken:" + userId, refreshToken, Duration.ofHours(refreshTokenExpiry));
        redisService.save("refreshTokenMap:" + refreshToken, userId, Duration.ofHours(refreshTokenExpiry));
        return refreshToken;
    }

    @Override
    public void deleteRefreshToken(String userId) {
        String token = redisService.get("refreshToken:" + userId);
        redisService.delete("refreshToken:" + userId);
        redisService.delete("refreshTokenMap:" + token);
    }

    @Override
    public String rotateRefreshToken(String userId, Integer refreshTokenExpiry) {
        deleteRefreshToken(userId);
        return createToken(userId, refreshTokenExpiry);
    }

    @Override
    public String getUserIdByToken(String token) {
        return redisService.get("refreshTokenMap:" + token);
    }

}
