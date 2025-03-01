package id.overridestudio.tixfestapi.feature.ticketmanagement.service;

import id.overridestudio.tixfestapi.feature.ticketmanagement.entity.Ticket;

public interface TicketService {

    Ticket getById(String id);
}
