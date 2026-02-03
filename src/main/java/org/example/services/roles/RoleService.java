package org.example.services.roles;

import org.example.enums.RoleName;
import org.example.model.Role;

public interface RoleService {
    Role getByName(RoleName roleName);
}
