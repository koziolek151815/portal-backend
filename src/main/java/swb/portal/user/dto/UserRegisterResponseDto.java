package swb.portal.user.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Builder
@Data
public class UserRegisterResponseDto {
    private Long id;
    private String username;
    private String email;
    private Instant userCreatedDate;
}
