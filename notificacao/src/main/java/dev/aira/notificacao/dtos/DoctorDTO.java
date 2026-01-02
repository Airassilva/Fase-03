package dev.aira.notificacao.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.aira.notificacao.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DoctorDTO {
    private UUID id;
    private String name;
    private String email;

    @JsonProperty("userType")
    private UserType type;
}
