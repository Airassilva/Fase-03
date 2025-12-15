package dev.aira.agendamento.user.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum UserType {
    MEDICO("medico"),
    ENFERMEIRO("enfermeiro"),
    PACIENTE("paciente");
    private String tipo;
}