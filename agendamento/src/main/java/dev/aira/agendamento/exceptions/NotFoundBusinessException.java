package dev.aira.agendamento.exceptions;

public class NotFoundBusinessException extends RuntimeException {
    public NotFoundBusinessException(String message) {
        super(message);
    }
}
