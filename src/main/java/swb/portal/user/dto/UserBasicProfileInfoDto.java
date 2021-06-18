package swb.portal.user.dto;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class UserBasicProfileInfoDto {

    private Long id;
    private String username;
    private String email;

}
