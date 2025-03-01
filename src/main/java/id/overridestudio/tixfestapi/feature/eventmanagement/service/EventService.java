package id.overridestudio.tixfestapi.feature.eventmanagement.service;

import id.overridestudio.tixfestapi.feature.eventmanagement.dto.request.EventRequest;
import id.overridestudio.tixfestapi.feature.eventmanagement.dto.response.EventResponse;
import org.springframework.data.domain.Page;

public interface EventService {
    //CREATE
    //Validate User has role EO
    //Validate User EO is verified
    //Check if EO has Event Draft
    //if it has Event Draft Edit
    //if it hasn't Create new Event Draft

    EventResponse create(EventRequest request);

    //TODO: Page<EventResponse> getAllEvent();
    //TODO: Page<EventResponse> getAllEventByEventOrganizer(String eventOrganizerId);
    //TODO: Page<EventResponse> getAllEventByEventCategory(String eventCategoryId);

    EventResponse update(EventRequest request);

    //TODO: CLEAR EVENT DRAFT;
    //TODO: SOFT-DELETE EVENT;
}
