package dev.aira.agendamento.exceptions;

public class InvalidDoctorUserException extends BadRequestBusinessException {
    public InvalidDoctorUserException() {
        super("User is not a doctor");
    }
}
