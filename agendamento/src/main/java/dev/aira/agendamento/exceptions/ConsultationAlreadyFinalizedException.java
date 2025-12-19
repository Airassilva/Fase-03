package dev.aira.agendamento.exceptions;

public class ConsultationAlreadyFinalizedException extends BadRequestBusinessException{
    public ConsultationAlreadyFinalizedException() {
        super("Consultation already finalized");
    }
}
