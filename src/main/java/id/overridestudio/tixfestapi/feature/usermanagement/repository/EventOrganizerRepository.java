package id.overridestudio.tixfestapi.feature.usermanagement.repository;

import id.overridestudio.tixfestapi.feature.usermanagement.entity.EventOrganizer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EventOrganizerRepository extends JpaRepository<EventOrganizer, String>, JpaSpecificationExecutor<EventOrganizer> {
    boolean existsByIdAndUserAccountId(String eventOrganizerId, String userId);
}
