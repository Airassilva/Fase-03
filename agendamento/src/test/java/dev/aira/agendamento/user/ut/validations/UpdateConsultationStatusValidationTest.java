package dev.aira.agendamento.user.ut.validations;

import dev.aira.agendamento.consultation.entities.Consultation;
import dev.aira.agendamento.consultation.validations.UpdateConsultationStatusValidation;
import dev.aira.agendamento.exceptions.ConsultationAlreadyFinalizedException;
import dev.aira.agendamento.objectMother.ConsultationMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UpdateConsultationStatusValidationTest {

    @Mock
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
    void test_update_consultation_status_invalid(){
        Consultation consultation = ConsultationMother.consultationBaseStatusFinalized();
        assertThrows(
                ConsultationAlreadyFinalizedException.class,
                () -> updateConsultationStatusValidation.validation(consultation)
        );
    }
}
