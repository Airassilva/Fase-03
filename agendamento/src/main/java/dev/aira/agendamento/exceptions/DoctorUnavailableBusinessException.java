package dev.aira.agendamento.exceptions;

public class DoctorUnavailableBusinessException extends UnavailableBusinessException {
    public DoctorUnavailableBusinessException() {
        super("Doctor has been unavailable");
    }
}
