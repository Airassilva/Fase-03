package dev.aira.agendamento.user.validations;

import dev.aira.agendamento.user.entities.User;

public interface UserCreateValidation {
    void valida(User user);
}
