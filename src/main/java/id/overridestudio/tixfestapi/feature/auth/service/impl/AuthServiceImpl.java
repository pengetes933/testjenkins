package id.overridestudio.tixfestapi.feature.auth.service.impl;

import id.overridestudio.tixfestapi.feature.auth.dto.config.TokenExpiryConfig;
import id.overridestudio.tixfestapi.feature.auth.dto.request.AuthRequest;
import id.overridestudio.tixfestapi.feature.auth.dto.response.AuthResponse;
import id.overridestudio.tixfestapi.feature.auth.service.AuthService;
import id.overridestudio.tixfestapi.feature.auth.service.JwtService;
import id.overridestudio.tixfestapi.feature.auth.service.RefreshTokenService;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.ForgotPasswordRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.ResetPasswordRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.VerifyOtpRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.entity.UserAccount;
import id.overridestudio.tixfestapi.feature.usermanagement.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserAccountService userAccountService;
    private final RefreshTokenService refreshTokenService;

    @Value("${tixfest.jwt-expiration-in-minutes-access-token}")
    private Long EXPIRATION_IN_MINUTES_ACCESS_TOKEN;

    @Value("${tixfest.refresh-token-expiration-in-hour}")
    private Integer REFRESH_TOKEN_EXPIRY;

    @Override
    public AuthResponse login(String clientType, AuthRequest request) {

        UserAccount userAccount = userAccountService.getOneByEmail(request.getEmail());
        if (userAccount == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid email");
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userAccount.getEmail(), request.getPassword()));
        if (!authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad Credentials");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        TokenExpiryConfig tokenExpiryConfig = getTokenExpiryConfig(clientType);
        String refreshToken = refreshTokenService.createToken(userAccount.getId(), tokenExpiryConfig.getRefreshTokenExpiry());
        String accessToken = jwtService.generateAccessToken(userAccount, tokenExpiryConfig.getAccessTokenExpiry());
        return AuthResponse.builder()
                .id(userAccount.getId())
                .accessToken(accessToken)
                .expiresIn(tokenExpiryConfig.getAccessTokenExpiry())
                .refreshToken(refreshToken)
                .refreshExpiresIn(tokenExpiryConfig.getRefreshTokenExpiry())
                .role(userAccount.getRole().getName())
                .build();
    }

    @Override
    public AuthResponse refreshToken(String clientType, String token) {
        String userId = refreshTokenService.getUserIdByToken(token);
        UserAccount userAccount = userAccountService.getOne(userId);
        TokenExpiryConfig tokenExpiryConfig = getTokenExpiryConfig(clientType);

        String newRefreshToken = refreshTokenService.rotateRefreshToken(userId, tokenExpiryConfig.getRefreshTokenExpiry());
        String newToken = jwtService.generateAccessToken(userAccount, tokenExpiryConfig.getAccessTokenExpiry());
        return AuthResponse.builder()
                .id(userAccount.getId())
                .accessToken(newToken)
                .expiresIn(tokenExpiryConfig.getAccessTokenExpiry())
                .refreshToken(newRefreshToken)
                .refreshExpiresIn(tokenExpiryConfig.getRefreshTokenExpiry())
                .role(userAccount.getRole().getName())
                .build();
    }

    @Override
    public void logout(String accessToken) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAccount userAccount = (UserAccount) authentication.getPrincipal();
        refreshTokenService.deleteRefreshToken(userAccount.getId());
        jwtService.blacklistAccessToken(accessToken);
    }

    @Override
    public void createResetPasswordRequest(ForgotPasswordRequest request) {
        userAccountService.forgotPasswordRequest(request);
    }

    @Override
    public void verifyOtpForgotPassword(VerifyOtpRequest request) {
        userAccountService.verifyOtpForgotPassword(request);
    }

    @Override
    public void passwordReset(ResetPasswordRequest request) {
        userAccountService.resetPassword(request);
    }

    private TokenExpiryConfig getTokenExpiryConfig(String clientType) {
        Long accessTokenExpiry;
        Integer refreshTokenExpiry;

        if ("WEB".equalsIgnoreCase(clientType)) {
            accessTokenExpiry = EXPIRATION_IN_MINUTES_ACCESS_TOKEN;
            refreshTokenExpiry = REFRESH_TOKEN_EXPIRY;
        } else if ("MOBILE".equalsIgnoreCase(clientType)) {
            accessTokenExpiry = EXPIRATION_IN_MINUTES_ACCESS_TOKEN * 288;
            refreshTokenExpiry = REFRESH_TOKEN_EXPIRY * 7;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid client type");
        }

        return new TokenExpiryConfig(accessTokenExpiry, refreshTokenExpiry);
    }
}
