package org.example.services.auth.impl;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.example.dto.user.UserRegistrationRequestDto;
import org.example.dto.user.UserResponseDto;
import org.example.enums.RoleName;
import org.example.exceptions.RegistrationException;
import org.example.mappers.UserMapper;
import org.example.model.Role;
import org.example.model.User;
import org.example.repositories.RoleRepository;
import org.example.services.auth.AuthenticationService;
import org.example.services.user.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request)
            throws RegistrationException {
        if (userService.existsByEmail(request.getEmail())) {
            throw new RegistrationException("Email already in use");
        }
        if (!request.getPassword().equals(request.getRepeatPassword())) {
            throw new RegistrationException("Passwords do not match");
        }
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));
        user.setRoles(Set.of(userRole));
        return userMapper.toDto(userService.save(user));
    }
}
