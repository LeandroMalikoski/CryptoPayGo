package com.cryptopaygo.config.service;

import com.cryptopaygo.config.entity.User;
import com.cryptopaygo.config.enums.Role;
import com.cryptopaygo.config.exception.UserNotFoundException;
import com.cryptopaygo.config.records.UserRegisterRequestDTO;
import com.cryptopaygo.config.records.UserResponseDTO;
import com.cryptopaygo.config.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO registerUser(UserRegisterRequestDTO dto) {

        var password = passwordEncoder.encode(dto.password());

        var user = new User(dto.name(), dto.email(), password, Role.fromString(dto.role()));

        user = userRepository.save(user);

        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getRole().getName());
    }

    public UserResponseDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getRole().name())
                )
                .orElseThrow(
                        () -> new UserNotFoundException("User with id " + id + " not found")
                );
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public List<UserResponseDTO> findAll() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getRole().name()))
                .toList();
    }
}