package dev.aira.agendamento.consultation.validations;

import dev.aira.agendamento.consultation.entities.Consultation;
import dev.aira.agendamento.consultation.repositories.ConsultationRepository;
import dev.aira.agendamento.exceptions.DoctorUnavailableBusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UpdateDoctorAvailabilityValidation implements ConsultationUpdateValidation {

    private final ConsultationRepository consultationRepository;

    @Override
    public void validation(Consultation consultation) {
        LocalDateTime start = consultation.getConsultationDate();
        LocalDateTime end = start.plusMinutes(60);

        boolean conflict =
                consultationRepository.existsByDoctorIdAndConsultationDateBetweenAndIdNot(
                        consultation.getDoctorId(),
                        start,
                        end,
                        consultation.getId()
                );
        if (conflict) {
            throw new DoctorUnavailableBusinessException();
        }
    }
}
