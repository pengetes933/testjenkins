package id.overridestudio.tixfestapi.feature.ticketmanagement.service.impl;

import id.overridestudio.tixfestapi.feature.ticketmanagement.entity.Ticket;
import id.overridestudio.tixfestapi.feature.ticketmanagement.repository.TicketRepository;
import id.overridestudio.tixfestapi.feature.ticketmanagement.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    @Override
    public Ticket getById(String id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ticket not found"));
    }
}
