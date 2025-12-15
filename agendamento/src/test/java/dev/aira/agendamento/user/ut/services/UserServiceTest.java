package dev.aira.agendamento.user.ut.services;

import dev.aira.agendamento.exceptions.EmailNotFoundException;
import dev.aira.agendamento.exceptions.UserNotFoundException;
import dev.aira.agendamento.objectMother.UserMother;
import dev.aira.agendamento.user.dtos.UserUpdateRequest;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
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
        assertThat(result.getUserType(), is(user.getUserType()));
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
        User user = UserMother.userBase();
        List<User> users = List.of(user);
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> page = new PageImpl<>(users, pageable, users.size());

        when(userRepository.findAll(pageable)).thenReturn(page);
        Page<User> result = userService.findAll(pageable);

        assertThat(result.getContent(), hasSize(1));
        assertThat(result.getContent().getFirst(), is(user));
        assertThat(result.getTotalElements(), is(1L));
    }

    @Test
    void test_update_user_with_id_not_found() {
        UUID id = UUID.randomUUID();
        UserUpdateRequest user =  UserMother.userUpdateRequest();
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(
                UserNotFoundException.class,
                () -> userService.update(id,user));
    }

    @Test
    void test_update_user() {
        UUID id = UUID.randomUUID();
        User existingUser = UserMother.userBase();
        UserUpdateRequest updateRequest = UserMother.userUpdateRequest();

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);
        User result = userService.update(id, updateRequest);
        verify(userRepository).findById(id);
        verify(userRepository).save(existingUser);
        assertThat(result, is(existingUser));
    }

    @Test
    void test_delete_user_with_id_not_found() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.delete(id));
    }

    @Test
    void test_delete_user(){
        UUID id = UUID.randomUUID();
        User user = UserMother.userBase();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        userService.delete(id);
        verify(userRepository).delete(user);
    }

}
