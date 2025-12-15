package dev.aira.agendamento.exceptions;

public class EmailNotFoundException extends NotFoundBusinessException {
    public EmailNotFoundException() {
        super("Email não encontrado no sistema!");
    }
}
