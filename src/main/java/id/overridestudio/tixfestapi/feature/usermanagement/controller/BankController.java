package id.overridestudio.tixfestapi.feature.usermanagement.controller;

import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.BankRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.response.BankResponse;
import id.overridestudio.tixfestapi.feature.usermanagement.service.BankService;
import id.overridestudio.tixfestapi.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/banks")
public class BankController {
    private final BankService bankService;

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createBank(@RequestBody BankRequest request){
        BankResponse bankResponse = bankService.create(request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, "Success create bank", bankResponse);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @PutMapping(path = "/{id}")
    public ResponseEntity<?> updateBank (@PathVariable String id, @RequestBody BankRequest request){
        BankResponse bankResponse = bankService.updateById(id, request);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Success update bank", bankResponse);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getById (@PathVariable String id){
        BankResponse bankResponse = bankService.getById(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Success find bank by id", bankResponse);
    }

    @GetMapping
    public ResponseEntity<?> getAll (){
        List<BankResponse> bankResponses = bankService.getAll();
        return ResponseUtil.buildResponse(HttpStatus.OK, "Success find all bank", bankResponses);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @PatchMapping(path = "/{id}")
    public ResponseEntity<?> deleteById (@PathVariable String id){
        bankService.deleteById(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Success delete bank by id", null);
    }
}
