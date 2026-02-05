package org.example.services.user;

import org.example.model.User;

public interface UserService {
    boolean existsByEmail(String email);

    User save(User user);

    Long getUserIdByEmail(String email);
}
