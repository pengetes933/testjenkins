package id.overridestudio.tixfestapi.feature.usermanagement.repository;

import id.overridestudio.tixfestapi.feature.usermanagement.entity.EventOrganizerAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventOrganizerAddressRepository extends JpaRepository<EventOrganizerAddress, String> {
    boolean existsByIdAndEventOrganizerIdAndEventOrganizer_UserAccount_Id(String eventOrganizerAddressId, String eventOrganizerId, String userId);
}
