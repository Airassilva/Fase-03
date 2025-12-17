package dev.aira.agendamento.exceptions;

public class ConsultationNotFoundException extends NotFoundBusinessException {
    public ConsultationNotFoundException() {
        super("Consulta não encontrada!");
    }
}
