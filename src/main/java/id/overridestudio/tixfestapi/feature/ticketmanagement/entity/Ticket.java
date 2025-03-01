package id.overridestudio.tixfestapi.feature.ticketmanagement.entity;

import id.overridestudio.tixfestapi.core.entity.BaseEntity;
import id.overridestudio.tixfestapi.feature.eventmanagement.entity.Event;
import id.overridestudio.tixfestapi.feature.eventmanagement.entity.Series;
import id.overridestudio.tixfestapi.feature.ticketmanagement.constants.DatabaseConstant;
import id.overridestudio.tixfestapi.feature.ticketmanagement.constants.TicketStatus;
import id.overridestudio.tixfestapi.feature.usermanagement.entity.EventOrganizer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = DatabaseConstant.TICKET_TABLE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Ticket extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 50)
    private String id;

    @ManyToOne
    @JoinColumn(name = "event_organizer_id", nullable = false)
    private EventOrganizer eventOrganizer;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToMany
    @JoinTable(
            name = DatabaseConstant.TICKET_SERIES_TABLE,
            joinColumns = @JoinColumn(name = "ticket_id"),
            inverseJoinColumns = @JoinColumn(name = "series_id")
    )
    private Set<Series> series = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "ticket_type_id", nullable = false)
    private TicketType type;

    @ManyToOne
    @JoinColumn(name = "ticket_category_id", nullable = false)
    private TicketCategory ticketCategory;

    @Column(name = "is_bundling", nullable = false)
    private Boolean isBundling;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "quota", nullable = false)
    private Integer quota;

    @Enumerated(EnumType.STRING)
    @Column(name = "ticket_status")
    private TicketStatus ticketStatus;
}
