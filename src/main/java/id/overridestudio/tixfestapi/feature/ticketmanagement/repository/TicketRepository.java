package id.overridestudio.tixfestapi.feature.ticketmanagement.repository;

import id.overridestudio.tixfestapi.feature.ticketmanagement.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, String> {
}
