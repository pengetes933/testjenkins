package id.overridestudio.tixfestapi.feature.usermanagement.service.impl;

import id.overridestudio.tixfestapi.core.service.OtpService;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.*;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.response.UserAccountResponse;
import id.overridestudio.tixfestapi.feature.usermanagement.entity.Role;
import id.overridestudio.tixfestapi.feature.usermanagement.entity.UserAccount;
import id.overridestudio.tixfestapi.feature.usermanagement.repository.UserAccountRepository;
import id.overridestudio.tixfestapi.feature.usermanagement.service.RoleService;
import id.overridestudio.tixfestapi.feature.usermanagement.service.UserAccountService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {
    private final UserAccountRepository userAccountRepository;
    private final RoleService roleService;
    private final OtpService otpService;
    private final PasswordEncoder passwordEncoder;
    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$";

    @Value("${tixfest.user-super-admin-email}")
    private String emailAdmin;

    @Value("${tixfest.user-super-admin-password}")
    private String passwordAdmin;

    @Transactional(rollbackFor = Exception.class)
    @PostConstruct
    public void initSuperAdmin() {
        boolean exist = userAccountRepository.existsByEmail(emailAdmin);
        if (exist) return;
        Role role = roleService.getOneByName("ROLE_SUPER_ADMIN");
        UserAccount userAccount = UserAccount.builder()
                .email(emailAdmin)
                .password(passwordEncoder.encode(passwordAdmin))
                .role(role)
                .isDeleted(false)
                .build();
        userAccountRepository.saveAndFlush(userAccount);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserAccountResponse create(UserAccountRequest request) {
        if (userAccountRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }
        Role role = roleService.getOneByName(request.getRole());
        UserAccount userAccount = UserAccount.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .isDeleted(false)
                .build();
        userAccountRepository.saveAndFlush(userAccount);
        return toResponse(userAccount);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<UserAccount> findByEmail(String email) {
        return userAccountRepository.findByEmail(email);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserAccount create(UserAccount userAccount, String role) {
        userAccount.setRole(roleService.getOneByName(role));
        userAccount.setPassword(passwordEncoder.encode(userAccount.getPassword()));
        return userAccountRepository.saveAndFlush(userAccount);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserAccount save(UserAccount userAccount) {
        userAccount.setPassword(passwordEncoder.encode(userAccount.getPassword()));
        return userAccountRepository.save(userAccount);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void forgotPasswordRequest(ForgotPasswordRequest request) {
        if (userAccountRepository.findByEmail(request.getEmail()).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email not registered");
        }
        otpService.sendOtp(request.getEmail());
    }

    @Transactional(readOnly = true)
    @Override
    public void verifyOtpForgotPassword(VerifyOtpRequest request) {
        if (!otpService.verifyOtp(request.getEmail(), request.getOtp())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP verification failed");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void resetPassword(ResetPasswordRequest request) {
        verifyOtpForgotPassword(VerifyOtpRequest.builder()
                .email(request.getEmail())
                .otp(request.getOtp())
                .build());

        otpService.deleteOtp(request.getEmail());

        UserAccount userAccount = getOneByEmail(request.getEmail());
        userAccount.setPassword(request.getPassword());
        userAccount.setUpdatedAt(LocalDateTime.now());

        userAccountRepository.save(userAccount);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void changePassword(ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAccount userAccount = (UserAccount) authentication.getPrincipal();

        if (!passwordEncoder.matches(request.getCurrentPassword(), userAccount.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid credential");
        }

        userAccount.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userAccount.setUpdatedAt(LocalDateTime.now());
        userAccountRepository.save(userAccount);
    }

    @Transactional(readOnly = true)
    @Override
    public UserAccount getOne(String id) {
        return userAccountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public UserAccount getOneByEmail(String email) {
        return userAccountRepository.findByEmail(email)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userAccountRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found"));
    }

    @Transactional(readOnly = true)
    @Override
    public void deleteAccount(String id) {
        UserAccount userAccount = getOne(id);
        userAccount.setIsDeleted(true);
        userAccount.setDeletedAt(LocalDateTime.now());
        userAccountRepository.save(userAccount);
    }

    @Override
    public boolean validatePassword(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        return pattern.matcher(password).matches();
    }

    private UserAccountResponse toResponse(UserAccount userAccount) {
        return UserAccountResponse.builder()
                .id(userAccount.getId())
                .email(userAccount.getEmail())
                .role(userAccount.getRole().getName())
                .build();
    }

}
