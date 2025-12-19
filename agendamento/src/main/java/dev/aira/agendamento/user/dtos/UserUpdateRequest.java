package dev.aira.agendamento.user.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserUpdateRequest {

    @Email(message = "Invalid email format!")
    private String email;

    @Size(min = 8, message = "The password must be at least 8 characters long")
    private String password;
}
