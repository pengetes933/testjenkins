package id.overridestudio.tixfestapi.feature.eventmanagement.entity;

import id.overridestudio.tixfestapi.core.entity.BaseEntity;
import id.overridestudio.tixfestapi.feature.eventmanagement.constants.DatabaseConstant;
import id.overridestudio.tixfestapi.feature.eventmanagement.constants.EventStatus;
import id.overridestudio.tixfestapi.feature.usermanagement.entity.Customer;
import id.overridestudio.tixfestapi.feature.usermanagement.entity.EventOrganizer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = DatabaseConstant.EVENT_TABLE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Event extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 50)
    private String id;

    @ManyToOne
    @JoinColumn(name = "event_organizer_id", nullable = false)
    private EventOrganizer eventOrganizer;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "excerpt", nullable = false)
    private String excerpt;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "city", nullable = false)
    private String city; //TODO: CITY API

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_status")
    private EventStatus eventStatus;

    @Column(name = "sumRating", nullable = false)
    private Integer sumRating;

    @Column(name = "countRating", nullable = false)
    private Integer countRating;

    @Column(name = "avgRating", nullable = false)
    private Long avgRating;

    @ManyToMany(mappedBy = "wishlists", cascade = CascadeType.ALL)
    private List<Customer> wishlists;
}
