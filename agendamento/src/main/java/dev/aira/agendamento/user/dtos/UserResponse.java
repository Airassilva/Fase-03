package dev.aira.agendamento.user.dtos;

import dev.aira.agendamento.user.entities.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class UserResponse {
    private UUID id;
    private String name;
    private String email;
    private UserType userType;
}
