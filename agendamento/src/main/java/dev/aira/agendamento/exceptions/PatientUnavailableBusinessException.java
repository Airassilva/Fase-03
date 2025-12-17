package dev.aira.agendamento.exceptions;

public class PatientUnavailableBusinessException extends UnavailableBusinessException {
    public PatientUnavailableBusinessException() {
        super("Patient has been unavailable");
    }
}
