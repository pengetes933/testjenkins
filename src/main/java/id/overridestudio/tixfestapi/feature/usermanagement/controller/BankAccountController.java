package id.overridestudio.tixfestapi.feature.usermanagement.controller;

import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.BankAccountRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.response.BankAccountResponse;
import id.overridestudio.tixfestapi.feature.usermanagement.service.BankAccountService;
import id.overridestudio.tixfestapi.util.ResponseUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/event-organizers/{eventOrganizerId}/bank-accounts")
@Tag(name = "Bank Account Management", description = "APIs for bank account management, including creating, updating, and get bank account")
public class BankAccountController {
    private final BankAccountService bankAccountService;

    @PreAuthorize("hasRole('EVENT_ORGANIZER') and @permissionEvaluationServiceImpl.hasAccessToEventOrganizer(#eventOrganizerId, authentication.principal.id)")
    @PostMapping
    public ResponseEntity<?> createBankAccount(@PathVariable String eventOrganizerId, @RequestBody BankAccountRequest request){
        BankAccountResponse bankAccountResponse = bankAccountService.create(eventOrganizerId, request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, "Success create bank account", bankAccountResponse);
    }

    @PreAuthorize("hasRole('EVENT_ORGANIZER') and @permissionEvaluationServiceImpl.hasAccessToBankAccount(#id, #eventOrganizerId, authentication.principal.id)")
    @PutMapping(path = "/{id}")
    public ResponseEntity<?> updateBankAccount (@PathVariable String id, @PathVariable String eventOrganizerId, @RequestBody BankAccountRequest request){
        BankAccountResponse bankAccountResponse = bankAccountService.updateById(id, request);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Success update bank account", bankAccountResponse);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN') or (hasRole('EVENT_ORGANIZER') and @permissionEvaluationServiceImpl.hasAccessToBankAccount(#id, #eventOrganizerId, authentication.principal.id))")
    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getById (@PathVariable String id, @PathVariable String eventOrganizerId){
        BankAccountResponse bankAccountResponse = bankAccountService.getById(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Success find bank account by id", bankAccountResponse);
    }
}
