package dev.aira.agendamento.exceptions;

public class UnavailableBusinessException extends RuntimeException {
    public UnavailableBusinessException(String message) {
        super(message);
    }
}
