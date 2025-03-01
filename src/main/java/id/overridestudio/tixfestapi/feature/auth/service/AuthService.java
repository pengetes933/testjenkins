package id.overridestudio.tixfestapi.feature.auth.service;

import id.overridestudio.tixfestapi.feature.auth.dto.request.AuthRequest;
import id.overridestudio.tixfestapi.feature.auth.dto.response.AuthResponse;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.ForgotPasswordRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.ResetPasswordRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.VerifyOtpRequest;

public interface AuthService {
    AuthResponse login(String clientType, AuthRequest request);
    AuthResponse refreshToken(String clientType, String token);
    void logout(String accessToken);
    void createResetPasswordRequest(ForgotPasswordRequest request);
    void verifyOtpForgotPassword(VerifyOtpRequest request);
    void passwordReset(ResetPasswordRequest request);
}
