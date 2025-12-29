package dev.aira.notificacao.exceptions;

public class BadRequestBusinessException extends RuntimeException {
    public BadRequestBusinessException(String message, Exception e) {
        super(message, e);
    }
}
