package id.overridestudio.tixfestapi.feature.usermanagement.controller;

import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.CustomerRegisterRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.CustomerUpdateRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.SearchCustomerRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.VerifyOtpRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.response.CustomerResponse;
import id.overridestudio.tixfestapi.feature.usermanagement.service.CustomerService;
import id.overridestudio.tixfestapi.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/customers")
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping(path = "register")
    public ResponseEntity<?> initiateRegistration(@RequestBody CustomerRegisterRequest request){
        customerService.registration(request);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Success initiate registration customer", null);
    }

    @PostMapping(path = "verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpRequest request) {
        CustomerResponse customerResponse = customerService.verifyRegister(request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, "Success initiate registration customer", customerResponse);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN') or (hasRole('CUSTOMER') and @permissionEvaluationServiceImpl.hasAccessToCustomer(#id, authentication.principal.id))")
    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getById (@PathVariable String id){
        CustomerResponse customerResponse = customerService.getById(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Success find customer by id", customerResponse);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllCustomer (
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "q", required = false) String query
    ){
        try {
            SearchCustomerRequest pagingAndSortingRequest = SearchCustomerRequest.builder()
                    .page(page)
                    .size(size)
                    .sortBy(sortBy)
                    .query(query)
                    .build();
            Page<CustomerResponse> customerResponses = customerService.getAll(pagingAndSortingRequest);
            return ResponseUtil.buildResponsePage(HttpStatus.OK, "Success get all event organizer", customerResponses);
        } catch (Exception e) {
            return ResponseUtil.buildResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    @PreAuthorize("hasRole('CUSTOMER') and @permissionEvaluationServiceImpl.hasAccessToCustomer(#id, authentication.principal.id)")
    @PutMapping(path = "/{id}")
    public ResponseEntity<?> updateCustomer (@PathVariable String id, @RequestBody CustomerUpdateRequest request){
        CustomerResponse customerResponse = customerService.updateById(id, request);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Success update customer", customerResponse);
    }

    @PreAuthorize("hasRole('CUSTOMER') and @permissionEvaluationServiceImpl.hasAccessToCustomer(#id, authentication.principal.id)")
    @PatchMapping(path = "/{id}")
    public ResponseEntity<?> deleteById (@PathVariable String id){
        customerService.deleteById(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Success delete customer by id", null);
    }
}
