package swb.portal.user.dto;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class UserRegisterRequestDto {

    private String username;
    private String password;
    private String email;

    private String avatarUrl;
    private String gender;
    private String profileDescription;

}
