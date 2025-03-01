package id.overridestudio.tixfestapi.feature.usermanagement.controller;

import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.ChangePasswordRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.UserAccountRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.response.UserAccountResponse;
import id.overridestudio.tixfestapi.feature.usermanagement.service.UserAccountService;
import id.overridestudio.tixfestapi.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/users")
public class UserAccountController {
    private final UserAccountService userAccountService;

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserAccountRequest request){
        UserAccountResponse userAccountResponse = userAccountService.create(request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, "Success create user", userAccountResponse);
    }

    @PatchMapping(path = "/change-password")
    public ResponseEntity<?> changePassword (@RequestBody ChangePasswordRequest request) {
        userAccountService.changePassword(request);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Success change password", null);
    }
}
