package org.example.services.auth;

import org.example.dto.user.UserRegistrationRequestDto;
import org.example.dto.user.UserResponseDto;

public interface AuthenticationService {
    UserResponseDto register(UserRegistrationRequestDto request);
}
