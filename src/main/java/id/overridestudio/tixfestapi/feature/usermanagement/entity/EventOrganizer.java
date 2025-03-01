package id.overridestudio.tixfestapi.feature.usermanagement.entity;

import id.overridestudio.tixfestapi.core.entity.BaseEntity;
import id.overridestudio.tixfestapi.feature.eventmanagement.entity.Event;
import id.overridestudio.tixfestapi.feature.usermanagement.constant.VerificationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(name = "m_event_organizer")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class EventOrganizer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36)
    private String id;

    @OneToOne
    @JoinColumn(name = "user_account_id", nullable = false)
    private UserAccount userAccount;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @OneToOne(mappedBy = "eventOrganizer", orphanRemoval = true)
    private EventOrganizerImage profilePicture;

    @OneToOne(mappedBy = "eventOrganizer", cascade = CascadeType.ALL)
    private BankAccount bankAccount;

    @OneToMany(mappedBy = "eventOrganizer", cascade = CascadeType.ALL)
    private List<ResponsiblePerson> responsiblePersons;

    @OneToOne(mappedBy = "eventOrganizer", cascade = CascadeType.ALL)
    private EventOrganizerAddress address;

    @OneToMany(mappedBy = "eventOrganizer", cascade = CascadeType.ALL)
    private List<Event> events;

    @ManyToMany(mappedBy = "followings", cascade = CascadeType.ALL)
    private List<Customer> followers;

    @Column(name = "total_follower", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer totalFollower;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private VerificationStatus status;

    @Column(name = "is_verified", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isVerified;
}
