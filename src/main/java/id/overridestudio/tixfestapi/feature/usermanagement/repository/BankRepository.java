package id.overridestudio.tixfestapi.feature.usermanagement.repository;

import id.overridestudio.tixfestapi.feature.usermanagement.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends JpaRepository<Bank, String> {
    boolean existsById (String id);
}
