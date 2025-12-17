package dev.aira.agendamento.exceptions;

public class InvalidPatientUserException extends BadRequestBusinessException {
    public InvalidPatientUserException() {
        super("User is not a patient");
    }
}
