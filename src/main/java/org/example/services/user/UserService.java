package org.example.services.user;

import org.example.dto.user.UserRegistrationRequestDto;
import org.example.dto.user.UserResponseDto;
import org.example.model.User;

public interface UserService {
    boolean existsByEmail(String email);

    User save(User user);

    UserResponseDto register(UserRegistrationRequestDto request);
}
