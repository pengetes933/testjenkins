package id.overridestudio.tixfestapi.feature.transactionprocessing.entity;

import id.overridestudio.tixfestapi.core.entity.BaseEntity;
import id.overridestudio.tixfestapi.feature.ticketmanagement.entity.Ticket;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "t_order_details")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class OrderDetails extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 50)
    private String id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @Column(nullable = false, columnDefinition = "int check (quantity > 0)")
    private Integer quantity;

    @Column(nullable = false, columnDefinition = "bigint check (price > 0)")
    private Long price;

    @Column(nullable = false, columnDefinition = "bigint check (price > 0)")
    private Long subtotal;
}
