package id.overridestudio.tixfestapi.feature.usermanagement.entity;

import id.overridestudio.tixfestapi.core.entity.BaseEntity;
import id.overridestudio.tixfestapi.feature.usermanagement.constant.VerificationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "m_responsible_person")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class ResponsiblePerson extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36)
    private String id;

    @ManyToOne
    @JoinColumn(name = "event_organizer_id", nullable = false)
    private EventOrganizer eventOrganizer;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "nik", nullable = false, unique = true)
    private String nik;

    @Column(name = "phone_number", length = 16, nullable = false, unique = true)
    private String phoneNumber;

    @OneToOne(mappedBy = "responsiblePerson", orphanRemoval = true)
    private ResponsiblePersonAddress address;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private VerificationStatus status;

    @Column(name = "is_verified", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isVerified;
}
