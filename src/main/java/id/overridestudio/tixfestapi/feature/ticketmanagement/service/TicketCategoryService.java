package id.overridestudio.tixfestapi.feature.ticketmanagement.service;

import id.overridestudio.tixfestapi.feature.ticketmanagement.dto.request.TicketCategoryRequest;
import id.overridestudio.tixfestapi.feature.ticketmanagement.dto.response.TicketCategoryResponse;
import org.springframework.data.domain.Page;

public interface TicketCategoryService {
    TicketCategoryResponse create(TicketCategoryRequest request); //TODO: DONE
    Page<TicketCategoryResponse> getAllByEvent(String eventId);
    TicketCategoryResponse update(String ticketCategoryId, TicketCategoryRequest request); //TODO: DONE
    boolean delete(String ticketCategoryId);
}
