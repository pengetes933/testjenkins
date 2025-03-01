package id.overridestudio.tixfestapi.core.service.impl;

import id.overridestudio.tixfestapi.core.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmailOtp(String email, String otp) {
        try {
            // Creating the HTML email body
            String htmlContent = "<html>" +
                                 "<body style='font-family: Arial, sans-serif; color: #333333;'>" +
                                 "<table width='100%' cellspacing='0' cellpadding='0' style='background-color: #f4f4f4; padding: 20px;'>" +
                                 "<tr>" +
                                 "<td>" +
                                 "<table align='center' width='600' cellspacing='0' cellpadding='20' style='background-color: #ffffff; border-radius: 8px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);'>" +
                                 "<tr>" +
                                 "<td style='text-align: center; padding-bottom: 20px;'>" +
                                 "<h2 style='color: #2d87f0;'>Email Verification</h2>" +
                                 "<p style='font-size: 16px;'>Dear User,</p>" +
                                 "</td>" +
                                 "</tr>" +
                                 "<tr>" +
                                 "<td style='font-size: 16px; line-height: 1.6;'>"+
                                 "<p style='color: #333333;'>We have received a request to verify your email address. Please use the One-Time Password (OTP) below to complete the verification process.</p>" +
                                 "<p style='font-size: 24px; font-weight: bold; color: #2d87f0;'>Your OTP: <span style='font-size: 30px; color: #000000;'>" + otp + "</span></p>" +
                                 "<p style='color: #333333;'>This OTP is valid for 5 minutes. If you did not request this, please ignore this message.</p>" +
                                 "<p style='color: #333333;'>Thank you for using our service!</p>" +
                                 "</td>" +
                                 "</tr>" +
                                 "<tr>" +
                                 "<td style='text-align: center; padding-top: 20px;'>" +
                                 "<p style='font-size: 14px; color: #777777;'>If you have any questions, feel free to contact our support team.</p>" +
                                 "</td>" +
                                 "</tr>" +
                                 "</table>" +
                                 "</td>" +
                                 "</tr>" +
                                 "</table>" +
                                 "</body>" +
                                 "</html>";

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject("Your Email Verification Code");
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
            log.info("OTP sent successfully to {}", email);
        } catch (Exception e) {
            log.error("Failed to send OTP to {}: {}", email, e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }

}
