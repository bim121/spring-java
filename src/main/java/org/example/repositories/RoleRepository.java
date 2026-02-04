package org.example.repositories;

import java.util.Optional;
import org.example.enums.RoleName;
import org.example.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
