package dev.aira.agendamento.exceptions;

public class InvalidConsultationTimeException extends BadRequestBusinessException {
    public InvalidConsultationTimeException(String message) {
        super(message);
    }
}
