package dev.aira.agendamento.user.ut.validations;

import dev.aira.agendamento.consultation.entities.Consultation;
import dev.aira.agendamento.consultation.repositories.ConsultationRepository;
import dev.aira.agendamento.consultation.validations.ConsultationPatientAvailabilityValidation;
import dev.aira.agendamento.exceptions.PatientUnavailableBusinessException;
import dev.aira.agendamento.objectMother.ConsultationMother;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static dev.aira.agendamento.consultation.validations.ConsultationDoctorAvailabilityValidation.CONSULTATION_DURATION_MINUTES;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsultationPatientAvailabilityValidationTest {

    @Mock
    private ConsultationRepository consultationRepository;

    @InjectMocks
    private ConsultationPatientAvailabilityValidation consultationPatientAvailabilityValidation;

    @Test
    void test_patient_available() {
        LocalDateTime validDateTime =
                LocalDateTime.now().plusDays(1)
                        .withHour(10)
                        .withMinute(0)
                        .withSecond(0)
                        .withNano(0);
        Consultation consultation = ConsultationMother.consultatioWithTime(validDateTime);
        LocalDateTime start = consultation.getConsultationDate();
        LocalDateTime end = start.plusMinutes(CONSULTATION_DURATION_MINUTES);

        when(consultationRepository
                .existsPatientConflict(consultation.getPatientId(), start, end))
                .thenReturn(false);

        assertDoesNotThrow(
                () -> consultationPatientAvailabilityValidation
                        .validation(consultation)
        );
    }

    @Test
    void test_patient_unavailable() {
        LocalDateTime validDateTime =
                LocalDateTime.now().plusDays(1)
                        .withHour(10)
                        .withMinute(0)
                        .withSecond(0)
                        .withNano(0);
        Consultation consultation = ConsultationMother.consultatioWithTime(validDateTime);
        LocalDateTime start = consultation.getConsultationDate();
        LocalDateTime end = start.plusMinutes(CONSULTATION_DURATION_MINUTES);

        when(consultationRepository
                .existsPatientConflict(consultation.getPatientId(), start, end))
                .thenReturn(true);

        assertThrows(
                PatientUnavailableBusinessException.class,
                () -> consultationPatientAvailabilityValidation.validation(consultation)
        );
    }
}
