package dev.aira.agendamento.exceptions;

public class BadRequestBusinessException extends RuntimeException {
    public BadRequestBusinessException(String message) {
        super(message);
    }
}
