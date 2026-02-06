package org.example.services.user.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.user.UserRegistrationRequestDto;
import org.example.dto.user.UserResponseDto;
import org.example.enums.RoleName;
import org.example.exceptions.EntityNotFoundException;
import org.example.exceptions.RegistrationException;
import org.example.mappers.UserMapper;
import org.example.model.Role;
import org.example.model.User;
import org.example.repositories.UserRepository;
import org.example.services.roles.RoleService;
import org.example.services.user.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final UserRepository userRepository;

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Long getUserIdByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: "
                        + email))
                .getId();
    }

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request) {
        if (existsByEmail(request.getEmail())) {
            throw new RegistrationException("Email already in use");
        }
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Role role = roleService.findByName(RoleName.ROLE_USER);
        user.setRoles(Set.of(role));
        return userMapper.toDto(userRepository.save(user));
    }
}
