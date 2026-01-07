package dev.aira.historico.exceptions;

import java.util.UUID;

public class ConsultationNotFoundException extends NotFoundException {
    public ConsultationNotFoundException(UUID consultationId) {
        super("Consultation with id: " + consultationId + " not found");
    }
}
