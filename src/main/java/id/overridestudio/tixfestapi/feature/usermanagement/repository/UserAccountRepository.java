package id.overridestudio.tixfestapi.feature.usermanagement.repository;

import id.overridestudio.tixfestapi.feature.usermanagement.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, String> {
    Optional<UserAccount> findByEmail(String email);
    boolean existsByEmail(String email);
}
