package id.overridestudio.tixfestapi.feature.usermanagement.entity;

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
@Table(name = "m_responsible_person_address")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class ResponsiblePersonAddress extends Address {
    @OneToOne
    @JoinColumn(name = "responsible_person_id", nullable = false)
    private ResponsiblePerson responsiblePerson;
}
