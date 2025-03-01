package id.overridestudio.tixfestapi.core.service;

public interface OtpService {
    void sendOtp(String email);
    boolean verifyOtp(String email, String otp);
    void deleteOtp(String email);
}
