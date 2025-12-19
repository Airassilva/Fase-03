package dev.aira.agendamento.user.ut.validations;

import dev.aira.agendamento.consultation.entities.Consultation;
import dev.aira.agendamento.consultation.repositories.ConsultationRepository;
import dev.aira.agendamento.consultation.validations.CreateConsultationDoctorAvailabilityValidation;
import dev.aira.agendamento.exceptions.DoctorUnavailableBusinessException;
import dev.aira.agendamento.objectMother.ConsultationMother;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static dev.aira.agendamento.consultation.validations.CreateConsultationDoctorAvailabilityValidation.CONSULTATION_DURATION_MINUTES;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateConsultationDoctorAvailabilityValidationTest {

    @Mock
    private ConsultationRepository  consultationRepository;

    @InjectMocks
    private CreateConsultationDoctorAvailabilityValidation createConsultationDoctorAvailabilityValidation;

    @Test
    void test_doctor_available() {
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
                .existsDoctorConflict(consultation.getDoctorId(), start, end))
                .thenReturn(false);

        assertDoesNotThrow(
                () -> createConsultationDoctorAvailabilityValidation
                        .validation(consultation)
        );
    }

    @Test
    void test_doctor_unavailable() {
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
                .existsDoctorConflict(consultation.getDoctorId(), start, end))
                .thenReturn(true);

        assertThrows(
                DoctorUnavailableBusinessException.class,
                () -> createConsultationDoctorAvailabilityValidation.validation(consultation)
        );
    }
}
