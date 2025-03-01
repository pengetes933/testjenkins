package id.overridestudio.tixfestapi.feature.usermanagement.entity;

import id.overridestudio.tixfestapi.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "m_event_organizer_address")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class EventOrganizerAddress extends Address {
    @OneToOne
    @JoinColumn(name = "event_organizer_id", nullable = false)
    private EventOrganizer eventOrganizer;
}
