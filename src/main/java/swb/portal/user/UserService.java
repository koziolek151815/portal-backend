package swb.portal.user;


import lombok.RequiredArgsConstructor;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import swb.portal.entities.RoleEntity;
import swb.portal.entities.UserEntity;
import swb.portal.exceptions.EmailAlreadyTakenException;
import swb.portal.role.RoleService;
import swb.portal.user.dto.UserRegisterRequestDto;
import swb.portal.user.dto.UserRegisterResponseDto;

import java.util.HashSet;
import java.util.Set;


@RequiredArgsConstructor
@Service(value = "userService")
public class UserService implements UserDetailsService {

    private final RoleService roleService;

    private final UserRepository userRepository;

    private final UserFactory factory;


    public UserDetails loadUserByUsername(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getAuthority(user));
    }

    private Set<SimpleGrantedAuthority> getAuthority(UserEntity user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRoles().forEach(roleEntity -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + roleEntity.getName()));
        });

        return authorities;
    }

    public UserEntity getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();

        return userRepository
                .findByEmail(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(principal.getUsername()));
    }

    public UserRegisterResponseDto save(UserRegisterRequestDto userRegisterDto) {
        UserEntity newUser = factory.registeDtorToEntity(userRegisterDto);
        if(userRepository.existsByEmail(userRegisterDto.getEmail())) {
            throw new EmailAlreadyTakenException(userRegisterDto.getEmail());
        }


        RoleEntity role = roleService.getUserRole();
        Set<RoleEntity> roleSet = new HashSet<>();
        roleSet.add(role);

        if (newUser.getEmail().split("@")[1].equals("admin.edu")) {
            role = roleService.getAdminRole();
            roleSet.add(role);
        }
        newUser.setRoles(roleSet);

        return factory.entityToRegisterResponseDto( userRepository.save(newUser) );
    }
}
