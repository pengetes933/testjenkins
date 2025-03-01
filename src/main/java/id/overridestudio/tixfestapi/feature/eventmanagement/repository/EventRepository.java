package id.overridestudio.tixfestapi.feature.eventmanagement.repository;

import id.overridestudio.tixfestapi.feature.eventmanagement.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, String> {
}
