package swb.portal.ad;

import lombok.Builder;
import lombok.Data;
import swb.portal.entities.UserEntity;
import swb.portal.user.dto.UserBasicProfileInfoDto;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.time.Instant;
@Builder
@Data
public class AdResponseDto {
    private Long id;
    private String content;
    private Instant adCreatedDate;
    private UserBasicProfileInfoDto user;

}
