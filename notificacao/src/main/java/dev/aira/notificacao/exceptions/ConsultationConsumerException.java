package dev.aira.notificacao.exceptions;

public class ConsultationConsumerException extends BadRequestBusinessException {
    public ConsultationConsumerException(String message, Exception e) {
        super(message, e);
    }
}
