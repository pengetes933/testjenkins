package id.overridestudio.tixfestapi.feature.usermanagement.service;

import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.*;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.response.UserAccountResponse;
import id.overridestudio.tixfestapi.feature.usermanagement.entity.UserAccount;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserAccountService extends UserDetailsService {
    UserAccountResponse create (UserAccountRequest request);
    UserAccount create (UserAccount userAccount, String role);
    UserAccount save (UserAccount userAccount);
    Optional<UserAccount> findByEmail (String email);
    UserAccount getOne(String id);
    void forgotPasswordRequest (ForgotPasswordRequest request);
    void verifyOtpForgotPassword(VerifyOtpRequest request);
    void resetPassword (ResetPasswordRequest resetPasswordRequest);
    void changePassword(ChangePasswordRequest request);
    UserAccount getOneByEmail(String email);
    void deleteAccount(String id);
    boolean validatePassword(String password);
}
