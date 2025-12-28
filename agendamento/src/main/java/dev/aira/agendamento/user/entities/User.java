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

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Document(collection = "usuarios")
@NoArgsConstructor
public class User {

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

    public void addId(){
        this.id = UUID.randomUUID();
    }

    public void update(UserUpdateRequest dto) {
        if (dto.getPassword() != null) {
            this.password = dto.getPassword();
        }
        if (dto.getEmail() != null) {
            this.email = dto.getEmail();
        }
    }

    public void deactivate() {
        if (Boolean.FALSE.equals(this.active)) {
            return;
        }
        this.active = false;
    }

    public void activate() {
        if (Boolean.TRUE.equals(this.active)) {
            return;
        }
        this.active = true;
    }

    public boolean isActive() {
        return active;
    }

    public User(String email, String password, String name, UserType userType) {
            this.email = email;
            this.password = password;
            this.name = name;
            this.userType = userType;
    }

    public User(UUID id,String email, String password, String name, UserType userType) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.userType = userType;
    }
}
