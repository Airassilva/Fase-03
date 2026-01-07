package dev.aira.historico.exceptions;

import java.time.LocalDateTime;

public class InvalidConsultationDateException extends BadRequestBusinessException {
    public InvalidConsultationDateException(LocalDateTime date) {
        super("Invalid consultation date " + date);
    }
}
