package org.example.services.auth;

import org.example.dto.user.UserLoginRequestDto;
import org.example.dto.user.UserLoginResponseDto;
import org.example.dto.user.UserRegistrationRequestDto;
import org.example.dto.user.UserResponseDto;

public interface AuthenticationService {
    UserLoginResponseDto login(UserLoginRequestDto request);

    UserResponseDto register(UserRegistrationRequestDto request);
}
