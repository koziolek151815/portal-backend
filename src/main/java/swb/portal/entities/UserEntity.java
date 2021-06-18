package swb.portal.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    private String email;

    @Column(name = "user_created_date")
    private Instant userCreatedDate;

    @Column(name = "avatar_url")
    private String avatarUrl;
    private String gender;

    @Column(name = "profile_description")
    private String profileDescription;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USER_ROLES",
            joinColumns = {
                    @JoinColumn(name = "USER_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "ROLE_ID")})
    private Set<RoleEntity> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<AdEntity> ads = new HashSet<>();

    @PrePersist
    void createdAt() {
        this.userCreatedDate = Instant.now();
    }
}
