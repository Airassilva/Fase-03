package dev.aira.agendamento.user.ut.services;

import dev.aira.agendamento.exceptions.EmailNotFoundException;
import dev.aira.agendamento.exceptions.UserNotFoundException;
import dev.aira.agendamento.objectMother.UserMother;
import dev.aira.agendamento.user.entities.User;
import dev.aira.agendamento.user.repositories.UserRepository;
import dev.aira.agendamento.user.service.UserService;
import dev.aira.agendamento.user.validations.UserCreateValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserCreateValidation validation;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup() {
        userService = new UserService(
                userRepository,
                List.of(validation)
        );
    }

    @Test
     void test_create_new_user() {
        User user = UserMother.userBase();
        when(userRepository.save(user)).thenReturn(user);
        User result = userService.create(user);
        assertThat(result.getEmail(), is(user.getEmail()));
        assertThat(result.getPassword(), is(user.getPassword()));
        assertThat(result.getName(), is(user.getName()));
        assertThat(result.getTypeUser(), is(user.getTypeUser()));
        verify(validation).valida(user);
    }

    @Test
    void test_with_user_when_email_exists() {
        User user = UserMother.userBase();
        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        User result = userService.findByEmail(user.getEmail());
        assertThat(result, is(user));
    }

    @Test
    void test_with_user_when_email_not_found() throws EmailNotFoundException {
        when(userRepository.findByEmail("teste@email.com"))
                .thenReturn(Optional.empty());

        assertThrows(
                EmailNotFoundException.class,
                () -> userService.findByEmail("teste@email.com")
        );
    }

    @Test
    void test_with_user_when_id_exists() {
        User user = UserMother.userBase();
        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        User result = userService.findById(user.getId());
        assertThat(result, is(user));
    }

    @Test
    void test_with_user_when_id_not_found() throws UserNotFoundException {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.findById(id));
    }

    @Test
    void test_findAll() {
        List<User> users = List.of(UserMother.userBase());
        when(userRepository.findAll()).thenReturn(users);
        List<User> result = userService.findAll();
        assertThat(result, is(users));
    }

    @Test
    void test_update_user() {
        User user = UserMother.userBase();
        when(userRepository.save(user)).thenReturn(user);
        User result = userService.update(user);
        assertThat(result, is(user));
    }

    @Test
    void test_delete_user(){
        UUID id = UUID.randomUUID();
        userService.delete(id);
        verify(userRepository).deleteById(id);
    }

}
