package dev.aira.agendamento.exceptions;

public class DoctorNotFoundException extends NotFoundBusinessException {
    public DoctorNotFoundException() {
        super("Doctor not found");
    }
}
