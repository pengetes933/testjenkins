package id.overridestudio.tixfestapi.feature.usermanagement.entity;

import id.overridestudio.tixfestapi.core.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "m_user_accounts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class UserAccount extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36)
    private String id;

    @Column(name = "email", length = 254, nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$",
            message = "Password must be at least 8 characters long, contain an uppercase letter, a lowercase letter, a digit, and a special character.")
    private String password;

    @OneToOne(mappedBy = "userAccount")
    private Customer customer;

    @OneToOne(mappedBy = "userAccount")
    private EventOrganizer eventOrganizer;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(length = 16, name = "last_login_device")
    private String lastLoginDevice;

    @Column(name = "is_googled")
    private Boolean isGoogled;

    private Boolean isDeleted;

    @PrePersist
    public void prePersist() {
        if (isDeleted == null) {
            isDeleted = false;
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<Role> myRoles = List.of(role);
        return myRoles.stream().map(userRole -> new SimpleGrantedAuthority(role.getName())).toList();
    }
    @Override
    public String getUsername() {
        return email;
    }


}
