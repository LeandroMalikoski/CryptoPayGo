package com.cryptopaygo.config.service;

import com.cryptopaygo.config.entity.User;
import com.cryptopaygo.config.enums.Role;
import com.cryptopaygo.config.exception.RegisterInvalidException;
import com.cryptopaygo.config.exception.RequestInvalidException;
import com.cryptopaygo.config.exception.UserNotFoundException;
import com.cryptopaygo.config.records.UserRegisterRequestDTO;
import com.cryptopaygo.config.records.UserResponseDTO;
import com.cryptopaygo.config.records.UserUpdateDTO;
import com.cryptopaygo.config.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Valida os dados da requisição e retorna erros, se houver
    public void bindingResultMaster(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", "));

            throw new RequestInvalidException(errorMessage);
        }
    }

    @Transactional
    // Registra um novo usuário, criptografando sua senha e convertendo o mesmo para um DTO
    public void registerUser(UserRegisterRequestDTO dto, BindingResult bindingResult) {

        bindingResultMaster(bindingResult);

        // Verifica se o e-mail já está cadastrado
        if (userRepository.existsByEmail(dto.email())) {
            throw new RegisterInvalidException("Email address already in use");
        }

        if (userRepository.existsByName(dto.name())) {
            throw new RegisterInvalidException("Name already in use");
        }

        var password = passwordEncoder.encode(dto.password());

        var user = new User(dto.name(), dto.email(), password, Role.fromString(dto.role()));

        userRepository.save(user);
    }

    // Retorna os detalhes de um usuário baseado no ID
    public UserResponseDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getRole().name())
                )
                .orElseThrow(
                        () -> new UserNotFoundException("User with id " + id + " not found")
                );
    }

    // Retorna todos os usuários no sistema, mapeando para DTOs
    public List<UserResponseDTO> findAll() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getRole().name()))
                .toList();
    }

    @Transactional
    public UserResponseDTO updateUserDetails(Long id, UserUpdateDTO dto, BindingResult bindingResult) {

        bindingResultMaster(bindingResult);

        var user = userRepository.getUserById(id);

        if (user == null) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }

        if (userRepository.existsByEmail(dto.email())) {
            throw new RegisterInvalidException("Email address already in use");
        }

        if (userRepository.existsByName(dto.name())) {
            throw new RegisterInvalidException("Name already in use");
        }

        // Atualiza apenas os campos presentes no DTO
        if (dto.name() != null) {
            user.setName(dto.name());
        }
        if (dto.email() != null) {
            user.setEmail(dto.email());
        }
        if (dto.password() != null) {
            user.setPassword(passwordEncoder.encode(dto.password()));
        }

        userRepository.save(user);

        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getRole().name());
    }

    @Transactional
    public void deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.delete(user.get());
        } else {
            throw new UserNotFoundException("User not found");
        }
    }
}