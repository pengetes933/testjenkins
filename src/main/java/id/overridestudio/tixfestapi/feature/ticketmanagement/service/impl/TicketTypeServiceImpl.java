package id.overridestudio.tixfestapi.feature.ticketmanagement.service.impl;

import id.overridestudio.tixfestapi.feature.ticketmanagement.dto.request.TicketTypeRequest;
import id.overridestudio.tixfestapi.feature.ticketmanagement.dto.response.TicketTypeResponse;
import id.overridestudio.tixfestapi.feature.ticketmanagement.entity.TicketType;
import id.overridestudio.tixfestapi.feature.ticketmanagement.repository.TicketTypeRepository;
import id.overridestudio.tixfestapi.feature.ticketmanagement.service.TicketTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketTypeServiceImpl implements TicketTypeService {

    private TicketTypeRepository ticketTypeRepository;

    @Override
    public TicketTypeResponse create(TicketTypeRequest request) {

        return toTicketTypeResponse(ticketTypeRepository.saveAndFlush(TicketType.builder()
                .name(request.getName())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .isOpen(true) //TODO: HARDCODED
                .build()));
    }

    //TODO:
    @Override
    public Page<TicketTypeResponse> getAllByEvent(String eventId) {
        return null;
    }

    @Override
    public TicketTypeResponse update(String ticketTypeId, TicketTypeRequest request) {
        Optional<TicketType> optionalTicketType = ticketTypeRepository.findById(ticketTypeId);
        if (optionalTicketType.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket Type not found.");
        }

        TicketType ticketType = optionalTicketType.get();

        ticketType.setName(request.getName());
        ticketType.setDescription(request.getDescription());
        ticketType.setStartDate(request.getStartDate());
        ticketType.setEndDate(request.getEndDate());
        ticketType.setIsOpen(request.getIsOpen());

        return toTicketTypeResponse(ticketTypeRepository.save(ticketType));
    }

    //TODO: SOFT_DELETE
    @Override
    public TicketTypeResponse delete(String ticketTypeId) {
        return null;
    }

    private TicketTypeResponse toTicketTypeResponse(TicketType ticketType) {
        return TicketTypeResponse.builder()
                .id(ticketType.getId())
                .name(ticketType.getName())
                .description(ticketType.getDescription())
                .startDate(ticketType.getStartDate())
                .endDate(ticketType.getEndDate())
                .IsOpen(ticketType.getIsOpen())
                .build();
    }
}
