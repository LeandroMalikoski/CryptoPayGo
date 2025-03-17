package com.cryptopaygo.config.entity;

import com.cryptopaygo.config.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

// Representa a entidade de User no sistema
// Esta classe é usada para autenticação no Spring Security e para persistência no banco de dados
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(length = 150)
    private String name;

    // E-mail do usuário - Usado como userName para login
    @Column(unique = true, length = 150)
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    // Armazena a role como string no banco, em vez de índice numérico
    @Enumerated(EnumType.STRING)
    private Role role;

    public User(@NotBlank @NotNull String name, @NotBlank @NotNull @Email String email, @NotBlank @NotNull String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Retorna a lista de permissões do usuário (roles)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.getName()));
    }

    // Retorno de e-mail, tendo em vista que é o meio de login ao invés do nome de usuário
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        // Compara os atributos principais para verificar se dois usuários são iguais
        return Objects.equals(id, user.id) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}