package id.overridestudio.tixfestapi.feature.transactionprocessing.repository;

import id.overridestudio.tixfestapi.feature.transactionprocessing.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {
}
