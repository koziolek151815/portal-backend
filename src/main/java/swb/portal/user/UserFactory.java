package swb.portal.user;


import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import swb.portal.entities.UserEntity;
import swb.portal.user.dto.UserBasicProfileInfoDto;
import swb.portal.user.dto.UserProfileDto;
import swb.portal.user.dto.UserRegisterRequestDto;
import swb.portal.user.dto.UserRegisterResponseDto;

import java.time.Instant;


@RequiredArgsConstructor
public class UserFactory {

    private final BCryptPasswordEncoder bcryptEncoder;


    public UserEntity registeDtorToEntity(UserRegisterRequestDto dto) {
        return UserEntity.builder()
                .email(dto.getEmail())
                .username(dto.getUsername())
                .password(bcryptEncoder.encode(dto.getPassword()))
                .userCreatedDate(Instant.now())
                .gender(dto.getGender())
                .profileDescription(dto.getProfileDescription())
                .avatarUrl(dto.getAvatarUrl())
                .build();
    }

    public UserProfileDto entityToUserProfileDto(UserEntity entity) {
        return UserProfileDto.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .username(entity.getUsername())
                .userCreatedDate(entity.getUserCreatedDate())
                .gender(entity.getGender())
                .profileDescription(entity.getProfileDescription())
                .avatarUrl(entity.getAvatarUrl())
                .build();
    }

    public UserRegisterResponseDto entityToRegisterResponseDto(UserEntity entity) {
        return UserRegisterResponseDto.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .username(entity.getEmail())
                .userCreatedDate(entity.getUserCreatedDate())
                .build();
    }

    public UserBasicProfileInfoDto entityToBasicUserProfileInfoDto(UserEntity entity) {
        return UserBasicProfileInfoDto.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .build();
    }
}
