package id.overridestudio.tixfestapi.feature.usermanagement.service.impl;

import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.AddressRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.response.EventOrganizerAddressResponse;
import id.overridestudio.tixfestapi.feature.usermanagement.entity.EventOrganizer;
import id.overridestudio.tixfestapi.feature.usermanagement.entity.EventOrganizerAddress;
import id.overridestudio.tixfestapi.feature.usermanagement.repository.EventOrganizerAddressRepository;
import id.overridestudio.tixfestapi.feature.usermanagement.service.EventOrganizerAddressService;
import id.overridestudio.tixfestapi.feature.usermanagement.service.EventOrganizerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EventOrganizerAddressServiceImpl implements EventOrganizerAddressService {
    private final EventOrganizerAddressRepository eventOrganizerAddressRepository;
    private final EventOrganizerService eventOrganizerService;
    @Transactional(rollbackFor = Exception.class)
    @Override
    public EventOrganizerAddressResponse create(String eventOrganizerId, AddressRequest request) {
        EventOrganizer eventOrganizer = eventOrganizerService.getOne(eventOrganizerId);
        EventOrganizerAddress eventOrganizerAddress = EventOrganizerAddress.builder()
                .eventOrganizer(eventOrganizer)
                .street(request.getStreet())
                .city(request.getCity())
                .province(request.getProvince())
                .postalCode(request.getPostalCode())
                .build();
        return toResponse(eventOrganizerAddressRepository.saveAndFlush(eventOrganizerAddress));
    }
    @Transactional(readOnly = true)
    @Override
    public EventOrganizerAddressResponse getById(String id) {
        return toResponse(getOne(id));
    }

    private EventOrganizerAddress getOne(String id) {
        return eventOrganizerAddressRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found"));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public EventOrganizerAddressResponse updateById(String id, AddressRequest request) {
        EventOrganizerAddress eventOrganizerAddress = getOne(id);
        eventOrganizerAddress.setStreet(request.getStreet());
        eventOrganizerAddress.setCity(request.getCity());
        eventOrganizerAddress.setProvince(request.getProvince());
        eventOrganizerAddress.setPostalCode(request.getPostalCode());
        eventOrganizerAddress.setUpdatedAt(LocalDateTime.now());
        return toResponse(eventOrganizerAddressRepository.save(eventOrganizerAddress));
    }

    @Override
    public boolean existByEventOrganizerAddressIdAndEventOrganizerIdAndUserId(String eventOrganizerAddressId, String eventOrganizerId, String userId) {
        return eventOrganizerAddressRepository.existsByIdAndEventOrganizerIdAndEventOrganizer_UserAccount_Id(eventOrganizerAddressId, eventOrganizerId, userId);
    }

    EventOrganizerAddressResponse toResponse (EventOrganizerAddress eventOrganizerAddress) {
        return EventOrganizerAddressResponse.builder()
                .id(eventOrganizerAddress.getId())
                .street(eventOrganizerAddress.getStreet())
                .city(eventOrganizerAddress.getCity())
                .province(eventOrganizerAddress.getProvince())
                .postalCode(eventOrganizerAddress.getPostalCode())
                .build();
    }
}
