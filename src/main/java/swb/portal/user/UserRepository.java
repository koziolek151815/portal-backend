package swb.portal.user;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swb.portal.entities.UserEntity;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    void deleteUserEntityByUsername(String test);

    Boolean existsByEmail(String email);
}
