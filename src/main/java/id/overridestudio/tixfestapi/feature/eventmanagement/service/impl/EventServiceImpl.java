package id.overridestudio.tixfestapi.feature.eventmanagement.service.impl;

import id.overridestudio.tixfestapi.feature.eventmanagement.constants.EventStatus;
import id.overridestudio.tixfestapi.feature.eventmanagement.dto.request.EventRequest;
import id.overridestudio.tixfestapi.feature.eventmanagement.dto.response.EventResponse;
import id.overridestudio.tixfestapi.feature.eventmanagement.entity.Event;
import id.overridestudio.tixfestapi.feature.eventmanagement.repository.EventRepository;
import id.overridestudio.tixfestapi.feature.eventmanagement.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Override
    public EventResponse create(EventRequest request) {

        //TODO: GET EVENT-ORGANIZER By Authentication Context THROWS ROLE NOT ACCEPTED
        //TODO: VALIDATE EVENT-ORGANIZER is VERIFIED THROWS EVENT-ORGANIZER IS NOT VERIFIED
        //TODO: CHECK IF EVENT-ORGANIZER HAS DRAFT EVENT

        Event event = Event.builder()
                .eventOrganizer(null) //TODO: AUTH CONTEXT
                .name(request.getName())
                .excerpt(request.getExcerpt())
                .description(request.getDescription())
                .city(request.getCity())
                .date(request.getDate())
                .eventStatus(EventStatus.DRAFT) //TODO: NOTES HARDCODED
                .sumRating(0)
                .countRating(0)
                .avgRating(0L)
                .build();

        eventRepository.saveAndFlush(event);

        return toEventResponse(event);
    }

    @Override
    public EventResponse update(EventRequest request) {
        return null;
    }

    private EventResponse toEventResponse(Event event) {
        return EventResponse.builder()
                .id(event.getId())
                .eventOrganizerId(event.getEventOrganizer().getId())
                .name(event.getName())
                .excerpt(event.getExcerpt())
                .description(event.getDescription())
                .city(event.getCity())
                .date(event.getDate())
                .eventStatus(event.getEventStatus())
                .sumRating(event.getSumRating())
                .countRating(event.getCountRating())
                .avgRating(event.getAvgRating())
                .build();
    }
}
