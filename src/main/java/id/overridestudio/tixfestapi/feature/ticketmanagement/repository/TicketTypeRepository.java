package id.overridestudio.tixfestapi.feature.ticketmanagement.repository;

import id.overridestudio.tixfestapi.feature.ticketmanagement.entity.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketTypeRepository extends JpaRepository<TicketType, String> {
}
