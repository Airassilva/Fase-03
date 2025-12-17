package dev.aira.agendamento.user.ut.validations;

import dev.aira.agendamento.consultation.entities.Consultation;
import dev.aira.agendamento.consultation.validations.CreateValidDoctorValidation;
import dev.aira.agendamento.exceptions.DoctorNotFoundException;
import dev.aira.agendamento.exceptions.InvalidDoctorUserException;
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
class CreateValidDoctorValidationTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CreateValidDoctorValidation createValidDoctorValidation;

    @Test
    void test_valid_doctor_creation() {
        UUID id = UUID.randomUUID();
        User user = UserMother.userSavedDoctor(id);
        Consultation consultation = ConsultationMother.consultationBase(id);

        when(userRepository.findById(consultation.getDoctorId())).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> createValidDoctorValidation.validation(consultation));
    }

    @Test
    void test_doctor_not_found() {
        UUID id = UUID.randomUUID();
        Consultation consultation = ConsultationMother.consultationBase(id);
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(
                DoctorNotFoundException.class,
                () -> createValidDoctorValidation.validation(consultation)
        );
    }

    @Test
    void test_user_is_not_doctor(){
        UUID id = UUID.randomUUID();
        User user = UserMother.userSaved(id);
        Consultation consultation = ConsultationMother.consultationBase(id);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        assertThrows(
                InvalidDoctorUserException.class,
                () -> createValidDoctorValidation.validation(consultation)
        );
    }
}
