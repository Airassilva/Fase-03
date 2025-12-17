package dev.aira.agendamento.exceptions;

public class InactiveException extends RuntimeException {
    public InactiveException(String message) {
        super(message);
    }
}
