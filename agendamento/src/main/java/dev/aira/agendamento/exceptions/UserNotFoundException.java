package dev.aira.agendamento.exceptions;

public class UserNotFoundException extends NotFoundBusinessException {
    public UserNotFoundException() {
        super("Usuário não encontrado");
    }
}
