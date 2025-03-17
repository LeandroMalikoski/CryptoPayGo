package com.cryptopaygo.config.controller;

import com.cryptopaygo.config.exception.RegisterInvalidException;
import com.cryptopaygo.config.records.UserRegisterRequestDTO;
import com.cryptopaygo.dto.GeneralResponseDTO;
import com.cryptopaygo.config.records.UserResponseDTO;
import com.cryptopaygo.config.service.UserService;
import jakarta.validation.Valid;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // Construtor para injeção de dependências
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Registre um novo usuário
    @PostMapping("/register")
    public ResponseEntity<GeneralResponseDTO> registerUser(@Valid @RequestBody UserRegisterRequestDTO dto, BindingResult bindingResult) {

        // Valida os dados de entrada de uma requisição
        if (bindingResult.hasErrors()) {
            // Se houver erros de validação, os detalhes dos erros são processados.
            String errorMessage = bindingResult.getFieldErrors().stream()
                    // Converte os erros de campo em mensagens de erro legíveis.
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    // Junta todas as mensagens de erro em uma única string separada por vírgula.
                    .collect(Collectors.joining(", "));

            // Retorna uma resposta HTTP com status 400 (BAD_REQUEST) e a lista de erros como corpo da resposta.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GeneralResponseDTO(errorMessage, false));
        }
        // Verifica se já existe um e-mail igual cadastrado no banco de dados
        if (userService.existsByEmail(dto.email())) {
            throw new RegisterInvalidException("Email address already in use");
        }

        // Registra o usuário novo no banco de dados
        userService.registerUser(dto);

        // Retorna status 201 (CREATED) com mensagem de confirmação
        return ResponseEntity.status(HttpStatus.CREATED).body(new GeneralResponseDTO("User registered successfully", true));
    }

    // Lista todos os usuários
    @GetMapping("/list")
    public ResponseEntity<List<UserResponseDTO>> getUsers() {

        // Obtém todos os usuários
        List<UserResponseDTO> userResponseDTOS = userService.findAll();

        // Retorna status 200 (OK) com a lista de usuários
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDTOS);
    }

    // Procura por um usuário pelo seu id
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserDetails(@PathVariable Long id) {

        // Procura pelo id informado na url
        var user = userService.getUserById(id);

        // Transforma em um novo UserResponseDTO
        var userResponseDTO = new UserResponseDTO(user);

        // Retorna status 200 (OK) com o UserResponseDTO criado
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDTO);

    }
}