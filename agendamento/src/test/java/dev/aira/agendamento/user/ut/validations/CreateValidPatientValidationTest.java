package dev.aira.agendamento.user.ut.validations;

import dev.aira.agendamento.consultation.entities.Consultation;
import dev.aira.agendamento.consultation.validations.CreateValidPatientValidation;
import dev.aira.agendamento.exceptions.InvalidPatientUserException;
import dev.aira.agendamento.exceptions.PatientNotFoundException;
import dev.aira.agendamento.objectMother.ConsultationMother;
import dev.aira.agendamento.objectMother.UserMother;
import dev.aira.agendamento.user.entities.User;
import dev.aira.agendamento.user.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateValidPatientValidationTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CreateValidPatientValidation createValidPatientValidation;

    @Test
    void test_create_patient_valid(){
        UUID patientId = UUID.randomUUID();
        User user = UserMother.userSaved(patientId);
        Consultation consultation = ConsultationMother.consultationBase(patientId);

        when(userRepository.findById(consultation.getPatientId())).thenReturn(Optional.of(user));

        assertDoesNotThrow(
                () -> createValidPatientValidation
                        .validation(consultation)
        );
    }

    @Test
    void test_patient_not_found() {
        UUID id = UUID.randomUUID();
        Consultation consultation = ConsultationMother.consultationBase(id);
        when(userRepository.findById(consultation.getPatientId())).thenReturn(Optional.empty());
        assertThrows(
                PatientNotFoundException.class,
                () -> createValidPatientValidation.validation(consultation)
        );
    }

    @Test
    void test_user_is_not_patient(){
        UUID id = UUID.randomUUID();
        User user = UserMother.userSavedDoctor(id);
        Consultation consultation = ConsultationMother.consultationBase(id);
        when(userRepository.findById(consultation.getPatientId())).thenReturn(Optional.of(user));
        assertThrows(
                InvalidPatientUserException.class,
                () -> createValidPatientValidation.validation(consultation)
        );
    }
}
