package id.overridestudio.tixfestapi.feature.auth.controller;

import id.overridestudio.tixfestapi.feature.auth.dto.request.AuthRequest;
import id.overridestudio.tixfestapi.feature.auth.dto.response.AuthResponse;
import id.overridestudio.tixfestapi.feature.auth.service.AuthService;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.ForgotPasswordRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.ResetPasswordRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.VerifyOtpRequest;
import id.overridestudio.tixfestapi.util.ResponseUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

@RestController
@RequestMapping(path = "api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Value("${tixfest.refresh-token-expiration-in-hour}")
    private Integer REFRESH_TOKEN_EXPIRY;


    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request,HttpServletRequest servletRequest, HttpServletResponse response) {
        AuthResponse authResponse = authService.login(servletRequest.getHeader("X-Client-Type"), request);
        setCookie(response, authResponse.getRefreshToken());
        return ResponseUtil.buildResponse(HttpStatus.OK, "login successfully", authResponse);
    }

    @PostMapping(path = "/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = getRefreshToken(request);
        AuthResponse authResponse = authService.refreshToken(request.getHeader("X-Client-Type"), refreshToken);
        setCookie(response, authResponse.getRefreshToken());
        return ResponseUtil.buildResponse(HttpStatus.OK, "Token Refreshed", authResponse);
    }

    private String getRefreshToken(HttpServletRequest request) {
        if (request.getCookies() != null) {
            Cookie cookie = Arrays.stream(request.getCookies())
                    .filter(c -> c.getName().equals("refreshToken"))
                    .findFirst()
                    .orElse(null);
            if (cookie != null) {
                return cookie.getValue();
            }
        }
        String tokenHeader = request.getHeader("X-Refresh-Token");
        if (tokenHeader != null) {
            return tokenHeader;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Refresh Token is Required");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        authService.logout(bearerToken);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Logout Successfully", null);
    }

    private void setCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * REFRESH_TOKEN_EXPIRY);
        response.addCookie(cookie);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> requestResetPassword(@RequestBody ForgotPasswordRequest request) {
        authService.createResetPasswordRequest(request);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Ok", null);
    }
    @PostMapping("/forgot-password/otp")
    public ResponseEntity<?> verifyOtpForgotPassword(@RequestBody VerifyOtpRequest request) {
        authService.verifyOtpForgotPassword(request);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Ok", null);
    }

    @PostMapping("/forgot-password/change-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request ){
        authService.passwordReset(request);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Ok", null);
    }
}
