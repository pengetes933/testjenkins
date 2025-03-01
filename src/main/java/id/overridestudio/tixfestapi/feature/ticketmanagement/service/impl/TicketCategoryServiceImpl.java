package id.overridestudio.tixfestapi.feature.ticketmanagement.service.impl;

import id.overridestudio.tixfestapi.feature.ticketmanagement.dto.request.TicketCategoryRequest;
import id.overridestudio.tixfestapi.feature.ticketmanagement.dto.response.TicketCategoryResponse;
import id.overridestudio.tixfestapi.feature.ticketmanagement.entity.TicketCategory;
import id.overridestudio.tixfestapi.feature.ticketmanagement.repository.TicketCategoryRepository;
import id.overridestudio.tixfestapi.feature.ticketmanagement.service.TicketCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketCategoryServiceImpl implements TicketCategoryService {

    private final TicketCategoryRepository ticketCategoryRepository;

    @Override
    public TicketCategoryResponse create(TicketCategoryRequest request) {

        TicketCategory ticketCategory = TicketCategory.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        return toTicketCategoryResponse(ticketCategoryRepository.saveAndFlush(ticketCategory));
    }

    //TODO
    @Override
    public Page<TicketCategoryResponse> getAllByEvent(String eventId) {
        return null;
    }

    @Override
    public TicketCategoryResponse update(String ticketCategoryId, TicketCategoryRequest request) {
        Optional<TicketCategory> optionalTicketCategory = ticketCategoryRepository.findById(ticketCategoryId);
        if (optionalTicketCategory.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket Category not found.");
        }

        TicketCategory ticketCategory = optionalTicketCategory.get();

        ticketCategory.setName(request.getName());
        ticketCategory.setDescription(request.getDescription());

        return toTicketCategoryResponse(ticketCategoryRepository.save(ticketCategory));
    }

    //TODO: SOFT_DELETE
    @Override
    public boolean delete(String ticketCategoryId) {
        return false;
    }

    private TicketCategoryResponse toTicketCategoryResponse(TicketCategory ticketCategory) {
        return TicketCategoryResponse.builder()
                .id(ticketCategory.getId())
                .name(ticketCategory.getName())
                .description(ticketCategory.getDescription())
                .build();
    }
}
