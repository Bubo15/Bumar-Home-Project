package project.bumar.service.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.bumar.data.entities.Role;
import project.bumar.data.repositories.RoleRepository;
import project.bumar.service.services.RoleService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    private void seedRoles() {
        if (roleRepository.findAll().isEmpty()) {
            roleRepository.saveAll(
                    new ArrayList<>() {{
                        add(new Role("ROLE_OWNER"));
                        add(new Role("ROLE_ADMIN"));
                        add(new Role("ROLE_MODERATOR"));
                        add(new Role("ROLE_USER"));
                    }});
        }
    }

    @Override
    public Role getRole(String role) {
        return roleRepository
                .findAll()
                .stream()
                .filter(r -> r.getAuthority().toLowerCase().contains(role.toLowerCase()))
                .findFirst()
                .orElse(new Role("USER"));
    }
}
