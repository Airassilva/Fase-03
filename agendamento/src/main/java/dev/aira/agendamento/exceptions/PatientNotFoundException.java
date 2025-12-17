package dev.aira.agendamento.exceptions;

public class PatientNotFoundException extends NotFoundBusinessException {
    public PatientNotFoundException() {
        super("Patient not found");
    }
}
