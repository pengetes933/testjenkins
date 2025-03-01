package id.overridestudio.tixfestapi.feature.usermanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.EventOrganizerRegisterRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.EventOrganizerUpdateRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.SearchEventOrganizerRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.VerifyOtpRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.response.EventOrganizerResponse;
import id.overridestudio.tixfestapi.feature.usermanagement.service.EventOrganizerService;
import id.overridestudio.tixfestapi.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/event-organizers")
public class EventOrganizerController {
    private final EventOrganizerService eventOrganizerService;
    private final ObjectMapper objectMapper;

    @PostMapping(path = "register")
    public ResponseEntity<?> initiateRegistration(@RequestBody EventOrganizerRegisterRequest request) {
        eventOrganizerService.registration(request);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Success initiate registration event organizer", null);
    }

    @PostMapping(path = "verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpRequest request) {
        EventOrganizerResponse eventOrganizerResponse = eventOrganizerService.verifyRegister(request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, "Success initiate registration event organizer", eventOrganizerResponse);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN') or (hasRole('EVENT_ORGANIZER') and @permissionEvaluationServiceImpl.hasAccessToEventOrganizer(#id, authentication.principal.id))")
    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        EventOrganizerResponse eventOrganizerResponse = eventOrganizerService.getById(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Success find event organizer by id", eventOrganizerResponse);
    }

    @GetMapping
    public ResponseEntity<?> getAllEventOrganizer(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "q", required = false) String query
    ) {
        try {
            SearchEventOrganizerRequest pagingAndSortingRequest = SearchEventOrganizerRequest.builder()
                    .page(page)
                    .size(size)
                    .sortBy(sortBy)
                    .query(query)
                    .build();
            Page<EventOrganizerResponse> eventOrganizerResponses = eventOrganizerService.getAll(pagingAndSortingRequest);
            return ResponseUtil.buildResponsePage(HttpStatus.OK, "Success get all event organizer", eventOrganizerResponses);
        } catch (Exception e) {
            return ResponseUtil.buildResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    @PreAuthorize("hasRole('EVENT_ORGANIZER') and @permissionEvaluationServiceImpl.hasAccessToEventOrganizer(#id, authentication.principal.id)")
    @PutMapping(path = "/{id}")
    public ResponseEntity<?> updateEventOrganizer(
            @PathVariable String id,
            @RequestPart(name = "profilePicture", required = false) MultipartFile multipartFile,
            @RequestPart(name = "updateProfile") String updateProfile
    ) {
        try {
            EventOrganizerUpdateRequest request = objectMapper.readValue(updateProfile, EventOrganizerUpdateRequest.class);
            EventOrganizerResponse eventOrganizerResponse = eventOrganizerService.updateById(id, request, multipartFile);
            return ResponseUtil.buildResponse(HttpStatus.OK, "Success update event organizer", eventOrganizerResponse);
        } catch (Exception e) {
            return ResponseUtil.buildResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    @PreAuthorize("hasRole('EVENT_ORGANIZER') and @permissionEvaluationServiceImpl.hasAccessToEventOrganizer(#id, authentication.principal.id)")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id) {
        eventOrganizerService.deleteById(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Success delete event organizer by id", null);
    }

    @PreAuthorize("hasRole('EVENT_ORGANIZER') and @permissionEvaluationServiceImpl.hasAccessToEventOrganizer(#id, authentication.principal.id)")
    @PostMapping(path = "/{id}/request-verify")
    public ResponseEntity<?> requestVerify(@PathVariable String id) {
        eventOrganizerService.requestVerify(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Success request verify", null);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @PostMapping(path = "/{id}/verify")
    public ResponseEntity<?> verifyEventOrganizer(@PathVariable String id) {
        EventOrganizerResponse eventOrganizerResponse = eventOrganizerService.verifyEventOrganizer(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Success verify event organizer", eventOrganizerResponse);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping(path = "{id}/follow")
    public ResponseEntity<?> followEventOrganizer(@PathVariable String id) {
        eventOrganizerService.toggleFollow(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Success follow event organizer", null);
    }
}
