package dev.aira.agendamento.user.dtos;

import dev.aira.agendamento.user.entities.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserRequest {
    @NotBlank(message = "Name is required!")
    private String name;

    @Email(message = "Invalid email format!")
    @NotBlank(message = "Email is required!")
    private String email;

    @NotBlank(message = "Password is required!")
    @Size(min = 8, message = "The password must be at least 8 characters long")
    private String password;

    @NotNull(message = "User type is required!")
    private UserType userType;
}
