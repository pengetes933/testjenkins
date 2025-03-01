package id.overridestudio.tixfestapi.feature.eventmanagement.entity;

import id.overridestudio.tixfestapi.core.entity.BaseEntity;
import id.overridestudio.tixfestapi.feature.eventmanagement.constants.DatabaseConstant;
import id.overridestudio.tixfestapi.feature.ticketmanagement.entity.Ticket;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = DatabaseConstant.SERIES_TABLE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Series extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 50)
    private String id;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToMany(mappedBy = "series")
    private Set<Ticket> tickets = new HashSet<>();

    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime start_date;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime end_date;
}
