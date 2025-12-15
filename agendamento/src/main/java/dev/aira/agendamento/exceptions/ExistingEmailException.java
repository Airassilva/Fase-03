package dev.aira.agendamento.exceptions;

public class ExistingEmailException extends BadRequestBusinessException {
    public ExistingEmailException() {
        super("Email já cadastrado!");
    }
}
