package dev.aira.agendamento.exceptions;

public class ConsultationAlreadyFinalizedCancelledException extends BadRequestBusinessException{
    public ConsultationAlreadyFinalizedCancelledException() {
        super("Consultation already finalized or canceled");
    }
}
