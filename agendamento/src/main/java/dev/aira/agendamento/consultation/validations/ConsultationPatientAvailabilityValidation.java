package dev.aira.agendamento.consultation.validations;

import dev.aira.agendamento.consultation.entities.Consultation;
import dev.aira.agendamento.consultation.repositories.ConsultationRepository;
import dev.aira.agendamento.exceptions.PatientUnavailableBusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ConsultationPatientAvailabilityValidation implements ConsultationCreateValidation{

    private final ConsultationRepository  consultationRepository;
    private static final int CONSULTATION_DURATION_MINUTES = 60;

    @Override
    public void validation(Consultation consultation) {
        LocalDateTime start = consultation.getConsultationDate();
        LocalDateTime end = start.plusMinutes(CONSULTATION_DURATION_MINUTES);

        boolean doctorHasConflict =
                consultationRepository.existsPatientConflict(consultation.getPatientId(), start, end);

        if (doctorHasConflict) {
            throw new PatientUnavailableBusinessException();
        }
    }
}
