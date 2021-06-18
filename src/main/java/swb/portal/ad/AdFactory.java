package swb.portal.ad;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import swb.portal.entities.AdEntity;
import swb.portal.entities.UserEntity;
import swb.portal.user.dto.UserBasicProfileInfoDto;

@RequiredArgsConstructor
public class AdFactory {
    public AdResponseDto entityToAdResponseDto(AdEntity adEntity) {
        return AdResponseDto.builder()
                .id(adEntity.getId())
                .content(adEntity.getContent())
                .adCreatedDate(adEntity.getAdCreatedDate())
                .user(UserBasicProfileInfoDto.builder()
                        .id(adEntity.getUser().getId())
                        .username(adEntity.getUser().getUsername())
                        .email(adEntity.getUser().getEmail()).build())
                .build();
    }

    public AdEntity adRequestDtoToEntity(AdRequestDto adRequestDto, UserEntity userEntity) {

        if(adRequestDto.getContent().length()>1000) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Content is too long. Max 1000 characters!");

        return AdEntity.builder()
                .content(adRequestDto.getContent())
                .user(userEntity)
                .build();
    }

    public AdEntity updateEntityFromAdRequestDto(AdEntity adEntity, AdRequestDto adRequestDto) {
        adEntity.setContent(adRequestDto.getContent());
        return adEntity;
    }
}
