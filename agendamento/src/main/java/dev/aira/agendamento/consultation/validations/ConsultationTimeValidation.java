package dev.aira.agendamento.consultation.validations;

import dev.aira.agendamento.consultation.entities.Consultation;
import dev.aira.agendamento.exceptions.InvalidConsultationTimeException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ConsultationTimeValidation implements ConsultationCreateValidation, ConsultationUpdateValidation{

    @Override
    public void validation(Consultation consultation) {
        LocalDateTime start = consultation.getConsultationDate();
        validateNotInPast(start);
        validateTimePattern(start);
        validateBusinessHours(start);
    }

    private void validateNotInPast(LocalDateTime dateTime) {
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new InvalidConsultationTimeException("Consultation cannot be in the past");
        }
    }

    private void validateTimePattern(LocalDateTime dateTime) {
        int minute = dateTime.getMinute();
        if (minute != 0) {
            throw new InvalidConsultationTimeException(
                    "Consultations must start at full hour (e.g. 13:00)"
            );
        }
    }

    private void validateBusinessHours(LocalDateTime dateTime) {
        int hour = dateTime.getHour();
        if (hour < 8 || hour >= 18) {
            throw new InvalidConsultationTimeException(
                    "Consultations must be scheduled between 08:00 and 18:00"
            );
        }
    }
}
