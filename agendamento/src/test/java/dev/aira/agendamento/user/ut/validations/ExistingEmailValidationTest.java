package dev.aira.agendamento.user.ut.validations;

import dev.aira.agendamento.exceptions.ExistingEmailException;
import dev.aira.agendamento.objectMother.UserMother;
import dev.aira.agendamento.user.entities.User;
import dev.aira.agendamento.user.repositories.UserRepository;
import dev.aira.agendamento.user.validations.ExistingEmailValidation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExistingEmailValidationTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    ExistingEmailValidation existingEmailValidation;

    @Test
    void test_create_new_user_with_not_existing_email() {
        User user = UserMother.userBase();
        when(userRepository.findByEmail("teste@email.com"))
                .thenReturn(Optional.empty());

        assertDoesNotThrow(() -> existingEmailValidation.valida(user));
    }

    @Test
    void test_create_new_user_with_existing_email() throws ExistingEmailException {
        User user = UserMother.userBase();
        User userExisting = UserMother.userExisting();

        when(userRepository.findByEmail("teste@email.com"))
                .thenReturn(Optional.of(userExisting));
        assertThrows(
                ExistingEmailException.class,
                () -> existingEmailValidation.valida(user)
        );
    }
}
