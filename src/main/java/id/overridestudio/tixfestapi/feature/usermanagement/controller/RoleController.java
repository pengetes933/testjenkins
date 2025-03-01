package id.overridestudio.tixfestapi.feature.usermanagement.controller;

import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.RoleRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.response.RoleResponse;
import id.overridestudio.tixfestapi.feature.usermanagement.service.RoleService;
import id.overridestudio.tixfestapi.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/roles")
public class RoleController {
    private final RoleService roleService;

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody RoleRequest request){
        RoleResponse roleResponse = roleService.create(request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, "Success create role", roleResponse);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @PutMapping(path = "/{id}")
    public ResponseEntity<?> updateRole (@PathVariable String id, @RequestBody RoleRequest request){
        RoleResponse roleResponse = roleService.update(id, request);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Success update role", roleResponse);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getById (@PathVariable String id){
        RoleResponse roleResponse = roleService.getById(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Success find role by id", roleResponse);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAll (){
        List<RoleResponse> roleResponses = roleService.getAll();
        return ResponseUtil.buildResponse(HttpStatus.OK, "Success find all role", roleResponses);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @PatchMapping(path = "/{id}")
    public ResponseEntity<?> deleteById (@PathVariable String id){
        roleService.delete(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Success delete role by id", null);
    }
}
