package dev.aira.notificacao.dtos;

import dev.aira.notificacao.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class DoctorDTO{
    private UUID id;
    private String name;
    private String email;
    private UserType type;
}
