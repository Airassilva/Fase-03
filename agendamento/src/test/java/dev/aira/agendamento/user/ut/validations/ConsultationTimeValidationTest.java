package dev.aira.agendamento.user.ut.validations;

import dev.aira.agendamento.consultation.entities.Consultation;
import dev.aira.agendamento.consultation.validations.ConsultationTimeValidation;
import dev.aira.agendamento.exceptions.InvalidConsultationTimeException;
import dev.aira.agendamento.objectMother.ConsultationMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class ConsultationTimeValidationTest {

    @Mock
    private ConsultationTimeValidation consultationTimeValidation;

    @BeforeEach
    void setUp() {
        consultationTimeValidation = new ConsultationTimeValidation();
    }

    @Test
    void test_time_valid(){
        LocalDateTime validDateTime =
                LocalDateTime.now().plusDays(1)
                                    .withHour(10)
                                    .withMinute(0)
                                    .withSecond(0)
                                    .withNano(0);
        Consultation consultation = ConsultationMother.consultatioWithTime(validDateTime);

        assertDoesNotThrow(
                    () -> consultationTimeValidation
                            .validation(consultation)
        );
    }

    @Test
    void test_invalid_time_in_the_past(){
        LocalDateTime invalidDateTime = LocalDateTime.now()
                .minusDays(1)
                .withHour(10)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
        Consultation consultation = ConsultationMother.consultatioWithTime(invalidDateTime);

        assertThatThrownBy(() -> consultationTimeValidation.validation(consultation))
                .isInstanceOf(InvalidConsultationTimeException.class)
                .hasMessage("Consultation cannot be in the past");
    }

    @Test
    void test_invalid_time_is_not_full_hour(){
        LocalDateTime notFullHour =
                LocalDateTime.now().plusDays(1)
                        .withHour(13)
                        .withMinute(30)
                        .withSecond(0)
                        .withNano(0);
        Consultation consultation = ConsultationMother.consultatioWithTime(notFullHour);

        assertThatThrownBy(() -> consultationTimeValidation.validation(consultation))
                .isInstanceOf(InvalidConsultationTimeException.class)
                .hasMessage("Consultations must start at full hour (e.g. 13:00)");
    }

    @Test
    void test_invalid_time_outside_bussines_hour(){
        LocalDateTime outsideBusinessHours =
                LocalDateTime.now().plusDays(1)
                        .withHour(20)
                        .withMinute(0)
                        .withNano(0);
        Consultation consultation = ConsultationMother.consultatioWithTime(outsideBusinessHours);

        assertThatThrownBy(() -> consultationTimeValidation.validation(consultation))
                .isInstanceOf(InvalidConsultationTimeException.class)
                .hasMessage("Consultations must be scheduled between 08:00 and 18:00");
    }
}
