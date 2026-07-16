package org.example.services.roles.impl;

import lombok.RequiredArgsConstructor;
import org.example.enums.RoleName;
import org.example.model.Role;
import org.example.repositories.RoleRepository;
import org.example.services.roles.RoleService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role findByName(RoleName roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() ->
                        new RuntimeException("Role " + roleName + " not found"));
    }
}
