package id.overridestudio.tixfestapi.feature.usermanagement.entity;

import id.overridestudio.tixfestapi.core.entity.File;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "m_event_organizer_image")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class EventOrganizerImage extends File {
    @OneToOne
    @JoinColumn(name = "event_organizer_id", nullable = false)
    private EventOrganizer eventOrganizer;
}
