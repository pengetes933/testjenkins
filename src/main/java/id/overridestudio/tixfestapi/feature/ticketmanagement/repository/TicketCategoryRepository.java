package id.overridestudio.tixfestapi.feature.ticketmanagement.repository;

import id.overridestudio.tixfestapi.feature.ticketmanagement.entity.TicketCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketCategoryRepository extends JpaRepository<TicketCategory, String> {
}
