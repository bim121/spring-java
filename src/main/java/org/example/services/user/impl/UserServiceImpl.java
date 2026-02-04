package org.example.services.user.impl;

import lombok.RequiredArgsConstructor;
import org.example.model.User;
import org.example.repositories.UserRepository;
import org.example.services.user.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}
