package id.overridestudio.tixfestapi.feature.usermanagement.service;

import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.AddressRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.response.EventOrganizerAddressResponse;

public interface EventOrganizerAddressService {
    EventOrganizerAddressResponse create (String eventOrganizerId, AddressRequest request);
    EventOrganizerAddressResponse getById(String id);
    EventOrganizerAddressResponse updateById(String id, AddressRequest request);
    boolean existByEventOrganizerAddressIdAndEventOrganizerIdAndUserId(String eventOrganizerAddressId, String eventOrganizerId, String userId);
}
