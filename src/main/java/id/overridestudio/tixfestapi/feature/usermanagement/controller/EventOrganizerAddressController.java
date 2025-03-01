package id.overridestudio.tixfestapi.feature.usermanagement.controller;

import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.AddressRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.response.EventOrganizerAddressResponse;
import id.overridestudio.tixfestapi.feature.usermanagement.service.EventOrganizerAddressService;
import id.overridestudio.tixfestapi.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/event-organizers/{eventOrganizerId}/addresses")
public class EventOrganizerAddressController {
    private final EventOrganizerAddressService eventOrganizerAddressService;

    @PreAuthorize("hasRole('EVENT_ORGANIZER') and @permissionEvaluationServiceImpl.hasAccessToEventOrganizer(#eventOrganizerId, authentication.principal.id)")
    @PostMapping
    public ResponseEntity<?> createAddress(@PathVariable String eventOrganizerId, @RequestBody AddressRequest request){
        EventOrganizerAddressResponse eventOrganizerAddressResponse = eventOrganizerAddressService.create(eventOrganizerId, request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, "Success create address", eventOrganizerAddressResponse);
    }

    @PreAuthorize("hasRole('EVENT_ORGANIZER') and @permissionEvaluationServiceImpl.hasAccessToEventOrganizerAddress(#id, #eventOrganizerId, authentication.principal.id)")
    @PutMapping(path = "/{id}")
    public ResponseEntity<?> updateAddress (@PathVariable String id, @PathVariable String eventOrganizerId, @RequestBody AddressRequest request){
        EventOrganizerAddressResponse eventOrganizerAddressResponse  = eventOrganizerAddressService.updateById(id, request);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Success update address", eventOrganizerAddressResponse);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getById (@PathVariable String id, @PathVariable String eventOrganizerId){
        EventOrganizerAddressResponse eventOrganizerAddressResponse  = eventOrganizerAddressService.getById(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Success find address by id", eventOrganizerAddressResponse);
    }
}
