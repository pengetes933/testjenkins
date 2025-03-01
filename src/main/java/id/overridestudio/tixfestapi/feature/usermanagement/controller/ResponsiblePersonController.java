package id.overridestudio.tixfestapi.feature.usermanagement.controller;

import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.ResponsiblePersonRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.response.ResponsiblePersonResponse;
import id.overridestudio.tixfestapi.feature.usermanagement.service.ResponsiblePersonService;
import id.overridestudio.tixfestapi.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/event-organizers/{eventOrganizerId}/responsible-persons")
public class ResponsiblePersonController {
    private final ResponsiblePersonService responsiblePersonService;

    @PreAuthorize("hasRole('EVENT_ORGANIZER') AND @permissionEvaluationServiceImpl.hasAccessToEventOrganizer(#eventOrganizerId, authentication.principal.id)")
    @PostMapping
    public ResponseEntity<?> createResponsiblePerson(@PathVariable String eventOrganizerId, @RequestBody ResponsiblePersonRequest request){
        ResponsiblePersonResponse responsiblePersonResponse = responsiblePersonService.create(eventOrganizerId, request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, "Success create responsible person", responsiblePersonResponse);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN') or (hasRole('EVENT_ORGANIZER') and @permissionEvaluationServiceImpl.hasAccessToBankAccount(#id, #eventOrganizerId, authentication.principal.id))")
    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getById(@PathVariable String id, @PathVariable String eventOrganizerId){
        ResponsiblePersonResponse responsiblePersonResponse = responsiblePersonService.getById(id);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, "Success find responsible person by id", responsiblePersonResponse);
    }

    @PreAuthorize("hasRole('EVENT_ORGANIZER') AND @permissionEvaluationServiceImpl.hasAccessToResponsiblePerson(#id, #eventOrganizerId, authentication.principal.id)")
    @PutMapping(path = "/{id}")
    public ResponseEntity<?> updateById(@PathVariable String id, @PathVariable String eventOrganizerId, ResponsiblePersonRequest request){
        ResponsiblePersonResponse responsiblePersonResponse = responsiblePersonService.updateById(id, request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, "Success update responsible person by id", responsiblePersonResponse);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN') or (hasRole('EVENT_ORGANIZER') and @permissionEvaluationServiceImpl.hasAccessToEventOrganizer(#eventOrganizerId, authentication.principal.id))")
    @GetMapping
    public ResponseEntity<?> getAllByEventOrganizerId (@PathVariable String eventOrganizerId){
        List<ResponsiblePersonResponse> responsiblePersonResponses = responsiblePersonService.getAllByEventOrganizer(eventOrganizerId);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Success find all role", responsiblePersonResponses);
    }

    @PreAuthorize("hasRole('EVENT_ORGANIZER') AND @permissionEvaluationServiceImpl.hasAccessToResponsiblePerson(#id, #eventOrganizerId, authentication.principal.id)")
    @PatchMapping(path = "/{id}")
    public ResponseEntity<?> deleteById (@PathVariable String id, @PathVariable String eventOrganizerId){
        responsiblePersonService.deleteById(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Success delete responsible person by id", null);
    }

    @PreAuthorize("hasRole('EVENT_ORGANIZER') AND @permissionEvaluationServiceImpl.hasAccessToResponsiblePerson(#id, #eventOrganizerId, authentication.principal.id)")
    @PostMapping(path = "/{id}/request-verify")
    public ResponseEntity<?> requestVerify (@PathVariable String id, @PathVariable String eventOrganizerId){
        responsiblePersonService.requestVerify(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Success request verify", null);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @PostMapping(path = "/{id}/verify")
    public ResponseEntity<?> verifyResponsiblePerson (@PathVariable String id){
        ResponsiblePersonResponse responsiblePersonResponse = responsiblePersonService.verifyResponsiblePerson(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Success verify event organizer", responsiblePersonResponse);
    }
}
