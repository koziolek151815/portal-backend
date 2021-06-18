package swb.portal.entities;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "ads")
public class AdEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 1000)
    private String content;
    private Instant adCreatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    @PrePersist
    void createdAt() {
        this.adCreatedDate = Instant.now();
    }
}
