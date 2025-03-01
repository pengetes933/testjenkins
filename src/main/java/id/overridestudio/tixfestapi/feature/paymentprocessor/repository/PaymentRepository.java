package id.overridestudio.tixfestapi.feature.paymentprocessor.repository;

import id.overridestudio.tixfestapi.feature.paymentprocessor.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, String> {

}
