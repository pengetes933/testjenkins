package id.overridestudio.tixfestapi.feature.usermanagement.repository;

import id.overridestudio.tixfestapi.feature.usermanagement.entity.ResponsiblePerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResponsiblePersonRepository extends JpaRepository<ResponsiblePerson, String> {
    boolean existsByIdAndNik (String id, String nik);
    boolean existsByPhoneNumber(String phoneNumber);
    List<ResponsiblePerson> findAllByEventOrganizerId (String eventOrganizerId);
    long countByEventOrganizerIdAndDeletedAtIsNull(String eventOrganizerId);
    boolean existsByIdAndEventOrganizerIdAndEventOrganizer_UserAccount_Id(String responsiblePersonId, String eventOrganizerId, String userId);
}
