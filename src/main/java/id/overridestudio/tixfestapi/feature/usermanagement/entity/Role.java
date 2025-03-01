package id.overridestudio.tixfestapi.feature.usermanagement.entity;

import id.overridestudio.tixfestapi.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "m_roles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Role extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36)
    private String id;

    @Column(length = 50, unique = true, nullable = false)
    private String name;
}
