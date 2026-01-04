package dev.aira.historico.exceptions;

public class BadRequestBusinessException extends RuntimeException {
    public BadRequestBusinessException(String message) {
        super(message);
    }
}
