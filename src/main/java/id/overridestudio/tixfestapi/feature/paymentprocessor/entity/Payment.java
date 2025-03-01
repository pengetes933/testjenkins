package id.overridestudio.tixfestapi.feature.paymentprocessor.entity;

import id.overridestudio.tixfestapi.core.entity.BaseEntity;
import id.overridestudio.tixfestapi.feature.transactionprocessing.entity.Order;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "t_payment")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 50)
    private String id;

    @OneToOne
    @JoinColumn(name = "order_id", unique = true, nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type")
    private PaymentType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus status;

    @Column(nullable = false)
    private Long amount;

}
