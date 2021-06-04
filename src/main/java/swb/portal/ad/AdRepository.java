package swb.portal.ad;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swb.portal.entities.AdEntity;

@Repository
public interface AdRepository extends JpaRepository<AdEntity,Long> {
}
