package dev.aira.agendamento.user.ut.validations;

import dev.aira.agendamento.consultation.entities.Consultation;
import dev.aira.agendamento.consultation.validations.UpdateConsultationStatusValidation;
import dev.aira.agendamento.exceptions.ConsultationAlreadyFinalizedCancelledException;
import dev.aira.agendamento.objectMother.ConsultationMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UpdateConsultationStatusValidationTest {

    UpdateConsultationStatusValidation updateConsultationStatusValidation;

    @BeforeEach
    void setUp() {
        updateConsultationStatusValidation = new UpdateConsultationStatusValidation();
    }

    @Test
    void test_update_consultation_status_valid(){
        UUID id = UUID.randomUUID();
        Consultation consultation = ConsultationMother.consultationBase(id);
        assertDoesNotThrow(
                () -> updateConsultationStatusValidation
                        .validation(consultation)
        );
    }

    @Test
    void test_update_consultation_status_finalized(){
        Consultation consultation = ConsultationMother.consultationBaseStatusFinalized();

        assertThrows(
                ConsultationAlreadyFinalizedCancelledException.class,
                () -> updateConsultationStatusValidation.validation(consultation)
        );
    }

    @Test
    void test_update_consultation_status_canceled(){
        Consultation consultation = ConsultationMother.consultationBaseStatusCancelled();

        assertThrows(
                ConsultationAlreadyFinalizedCancelledException.class,
                () -> updateConsultationStatusValidation.validation(consultation)
        );
    }
}
