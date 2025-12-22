package dev.aira.agendamento.consultation.validations;

import dev.aira.agendamento.consultation.entities.Consultation;
import dev.aira.agendamento.consultation.entities.ConsultationStatus;
import dev.aira.agendamento.exceptions.ConsultationAlreadyFinalizedCancelledException;
import org.springframework.stereotype.Component;

@Component
public class UpdateConsultationStatusValidation implements ConsultationUpdateValidation {
    @Override
    public void validation(Consultation consultation) {
        if (consultation.getStatus() == ConsultationStatus.FINALIZADA
            || consultation.getStatus() == ConsultationStatus.CANCELADA) {
            throw new ConsultationAlreadyFinalizedCancelledException();
        }
    }
}
