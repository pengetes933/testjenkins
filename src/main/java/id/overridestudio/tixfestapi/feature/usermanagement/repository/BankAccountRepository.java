package id.overridestudio.tixfestapi.feature.usermanagement.repository;

import id.overridestudio.tixfestapi.feature.usermanagement.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, String > {
    boolean existsByIdAndEventOrganizerIdAndEventOrganizer_UserAccount_Id(String bankAccountId, String eventOrganizerId, String userId);
}
