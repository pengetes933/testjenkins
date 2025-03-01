package id.overridestudio.tixfestapi.core.service.impl;

import id.overridestudio.tixfestapi.core.service.EmailService;
import id.overridestudio.tixfestapi.core.service.OtpService;
import id.overridestudio.tixfestapi.core.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpServiceImpl implements OtpService {

    private final RedisService redisService;
    private final EmailService emailService;

    private static final long OTP_EXPIRATION_SECONDS = 300;
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = 3000;

    @Override
    public void sendOtp(String email) {
        String redisKey = getRedisKey(email);

        if (Boolean.TRUE.equals(redisService.isExists(redisKey))) {
            log.warn("OTP request already sent for {}. Please wait until the current OTP expires.", email);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "OTP request already sent. Please wait until 5 minutes");
        }

        String otp = generateOtp();

        redisService.save(redisKey, otp, Duration.ofSeconds(OTP_EXPIRATION_SECONDS));

        int retries = 0;
        boolean sent = false;
        while (retries < MAX_RETRIES && !sent) {
            try {
                emailService.sendEmailOtp(email, otp);
                sent = true;
            } catch (Exception e) {
                retries++;
                log.error("Attempt {} failed to send OTP to {}. Retrying in {} ms", retries, email, RETRY_DELAY_MS);
                if (retries < MAX_RETRIES) {
                    try {
                        Thread.sleep(RETRY_DELAY_MS);
                    } catch (InterruptedException interruptedException) {
                        Thread.currentThread().interrupt();
                        log.error("Retry interrupted while sending OTP to {}", email);
                        throw new IllegalStateException("OTP sending process was interrupted. Please try again later.");
                    }
                } else {
                    log.error("Failed to send OTP after {} attempts to {}", retries, email);
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to send OTP after multiple attempts.");
                }
            }
        }
    }


    @Override
    public boolean verifyOtp(String email, String otp) {
        String redisKey = getRedisKey(email);
        String storedOtp = redisService.get(redisKey);

        if (storedOtp == null) {
            log.error("OTP for {} has expired or does not exist.", email);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP has expired or does not exist");
        }

        if (!storedOtp.equals(otp)) {
            log.error("Invalid OTP provided for {}.", email);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid OTP");
        }

        return true;
    }

    @Override
    public void deleteOtp(String email) {
        String redisKey = getRedisKey(email);
        redisService.delete(redisKey);
        log.info("OTP for {} has been deleted from Redis.", email);
    }

    private String getRedisKey(String email) {
        return "OTP:" + email;
    }

    private String generateOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }

}
