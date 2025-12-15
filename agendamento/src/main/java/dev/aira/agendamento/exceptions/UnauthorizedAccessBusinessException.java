package dev.aira.agendamento.exceptions;

public class UnauthorizedAccessBusinessException extends RuntimeException {
    public UnauthorizedAccessBusinessException(String message) {
        super(message);
    }
}
