package dev.aira.agendamento.user.entities;

import dev.aira.agendamento.user.dtos.UserUpdateRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@NoArgsConstructor
@Document(collection = "usuarios")
public class User implements UserDetails {

    @Id
    private UUID id;

    @NotBlank
    @Email
    @Indexed(unique = true)
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String name;

    private Boolean active = true;

    @NotNull
    private UserType userType;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.userType.name()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    public void update(UserUpdateRequest dto) {
        if (dto.getPassword() != null) {
            this.password = dto.getPassword();
        }
        if (dto.getEmail() != null) {
            this.email = dto.getEmail();
        }
    }

    public User(String email, String password, String name, UserType userType) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.userType = userType;
    }
}
