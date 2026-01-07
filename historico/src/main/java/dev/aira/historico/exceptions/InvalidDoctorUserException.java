package dev.aira.historico.exceptions;

import java.util.UUID;

public class InvalidDoctorUserException extends BadRequestBusinessException {
    public InvalidDoctorUserException(UUID id) {
        super("User is not a doctor with id: " + id);
    }
}
