package swb.portal.role;


import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import swb.portal.entities.RoleEntity;

import java.util.Optional;


@RequiredArgsConstructor
@Service(value = "roleService")
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleEntity getUserRole(){
        Optional<RoleEntity> role = roleRepository.findRoleByName("user");
        if(role.isPresent()) return role.get();

        RoleEntity newRole = RoleEntity.builder()
                .name("user")
                .description("test")
                .build();
        roleRepository.save(newRole);
        return newRole;
    }

    public RoleEntity getAdminRole(){
        Optional<RoleEntity> role = roleRepository.findRoleByName("admin");
        if(role.isPresent()) return role.get();

        RoleEntity newRole = RoleEntity.builder()
                .name("admin")
                .description("test")
                .build();
        roleRepository.save(newRole);
        return newRole;
    }
}
