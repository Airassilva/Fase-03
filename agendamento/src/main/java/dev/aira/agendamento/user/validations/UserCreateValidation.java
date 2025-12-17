package dev.aira.agendamento.user.validations;

import dev.aira.agendamento.user.entities.User;

public interface UserCreateValidation {
    void validation(User user);
}
