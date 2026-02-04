package org.example.services.auth.impl;

import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.example.dto.user.UserRegistrationRequestDto;
import org.example.dto.user.UserResponseDto;
import org.example.enums.RoleName;
import org.example.exceptions.RegistrationException;
import org.example.mappers.UserMapper;
import org.example.model.Role;
import org.example.model.User;
import org.example.services.auth.AuthenticationService;
import org.example.services.roles.RoleService;
import org.example.services.user.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request) {
        if (userService.existsByEmail(request.getEmail())) {
            throw new RegistrationException("Email already in use");
        }
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Role userRole = roleService.findByName(RoleName.ROLE_USER);
        user.setRoles(Set.of(userRole));
        return userMapper.toDto(userService.save(user));
    }
}
