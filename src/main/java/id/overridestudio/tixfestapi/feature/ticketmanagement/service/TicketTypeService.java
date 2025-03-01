package id.overridestudio.tixfestapi.feature.ticketmanagement.service;

import id.overridestudio.tixfestapi.feature.ticketmanagement.dto.request.TicketTypeRequest;
import id.overridestudio.tixfestapi.feature.ticketmanagement.dto.response.TicketTypeResponse;
import org.springframework.data.domain.Page;

public interface TicketTypeService {
    TicketTypeResponse create(TicketTypeRequest request); //TODO: DONE
    Page<TicketTypeResponse> getAllByEvent(String eventId);
    TicketTypeResponse update(String ticketTypeId, TicketTypeRequest request); //TODO: DONE
    TicketTypeResponse delete(String ticketTypeId);
}
