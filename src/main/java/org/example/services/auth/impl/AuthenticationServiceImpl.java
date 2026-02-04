package org.example.services.auth.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.user.UserRegistrationRequestDto;
import org.example.dto.user.UserResponseDto;
import org.example.exceptions.RegistrationException;
import org.example.mappers.UserMapper;
import org.example.model.User;
import org.example.services.auth.AuthenticationService;
import org.example.services.user.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request) {
        if (userService.existsByEmail(request.getEmail())) {
            throw new RegistrationException("Email already in use");
        }
        User user = userMapper.toEntity(request);
        userService.save(user);
        return userMapper.toDto(user);
    }
}
