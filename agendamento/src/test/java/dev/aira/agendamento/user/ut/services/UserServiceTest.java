package dev.aira.agendamento.user.ut.services;

import dev.aira.agendamento.exceptions.EmailNotFoundException;
import dev.aira.agendamento.exceptions.UserInactiveException;
import dev.aira.agendamento.exceptions.UserNotFoundException;
import dev.aira.agendamento.objectMother.UserMother;
import dev.aira.agendamento.user.dtos.LoginRequest;
import dev.aira.agendamento.user.dtos.LoginResponse;
import dev.aira.agendamento.user.dtos.UserUpdateRequest;
import dev.aira.agendamento.user.entities.User;
import dev.aira.agendamento.user.repositories.UserRepository;
import dev.aira.agendamento.user.service.TokenService;
import dev.aira.agendamento.user.service.UserService;
import dev.aira.agendamento.user.validations.UserCreateValidation;
import dev.aira.agendamento.user.validations.UserUpdateValidation;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserCreateValidation validation;

    @Mock
    private UserUpdateValidation updateValidation;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private TokenService  tokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup() {
        userService = new UserService(
                userRepository,
                passwordEncoder,
                tokenService,
                authenticationManager,
                List.of(validation),
                List.of(updateValidation)
        );
    }

    @Test
     void test_create_new_user() {
        User user = UserMother.userBase();
        when(userRepository.save(user)).thenReturn(user);
        when(passwordEncoder.encode(any())).thenReturn("senha-criptografada");

        User result = userService.create(user);

        assertThat(result.getEmail(), is(user.getEmail()));
        assertThat(result.getPassword(), is(user.getPassword()));
        assertThat(result.getName(), is(user.getName()));
        assertThat(result.getUserType(), is(user.getUserType()));

        verify(validation).validation(user);
        verify(passwordEncoder).encode(any());
    }

    @Test
    void test_create_user_when_email_not_found() throws EmailNotFoundException {
        when(userRepository.findByEmail("teste@email.com"))
                .thenReturn(Optional.empty());

        assertThrows(
                EmailNotFoundException.class,
                () -> userService.findByEmail("teste@email.com")
        );
    }

    @Test
    void test_create_user_when_id_not_found() throws UserNotFoundException {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.findById(id));
    }

    @Test
    void test_login_success() {
        User user = UserMother.userBase();
        user.addId();
        LoginRequest loginRequest =
                new LoginRequest(user.getEmail(), "1234");
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal())
                .thenReturn(user);
        when(tokenService.generateToken(user))
                .thenReturn(new LoginResponse("token-fake", 300L));

        LoginResponse response = userService.login(loginRequest);

        assertThat(response.accessToken(), is("token-fake"));
        assertThat(response.expiresIn(), is(300L));

        verify(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService).generateToken(user);
    }

    @Test
    void test_login_email_not_found() {
        LoginRequest loginRequest =
                new LoginRequest("email@inexistente.com", "123456");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(
                BadCredentialsException.class,
                () -> userService.login(loginRequest)
        );

        verify(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void test_login_invalid_password() {
        LoginRequest loginRequest =
                new LoginRequest("email@teste.com", "senha-errada");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(
                BadCredentialsException.class,
                () -> userService.login(loginRequest)
        );

        verify(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
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
        assertThat(result.getTotalElements(), is(1L));
        verify(userRepository, times(1)).findAll(pageable);
    }

    @Test
    void test_update_user_with_id_not_found() {
        UUID id = UUID.randomUUID();
        UserUpdateRequest user =  UserMother.userUpdateRequest();
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(
                UserNotFoundException.class,
                () -> userService.update(id,user));
        verify(userRepository).findById(id);
    }

    @Test
    void test_update_user_is_inactive() throws UserInactiveException {
        UserUpdateRequest userUpdateRequest = UserMother.userUpdateRequest();
        User user = UserMother.userInactive();
        UUID id = user.getId();
        when(userRepository.findById(id))
                .thenReturn(Optional.of(user));
        doThrow(UserInactiveException.class)
                .when(updateValidation).validation(user);

        assertThrows(
                UserInactiveException.class,
                () -> userService.update(id,userUpdateRequest)
        );

        verify(userRepository).findById(user.getId());
        verify(updateValidation).validation(user);
    }

    @Test
    void test_update_user() {
        UUID id = UUID.randomUUID();
        User existingUser = UserMother.userBase();
        UserUpdateRequest updateRequest = UserMother.userUpdateRequest();

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);
        when(passwordEncoder.encode(any())).thenReturn("senha-criptografada");

        User result = userService.update(id, updateRequest);

        verify(userRepository).findById(id);
        verify(userRepository).save(existingUser);
        verify(passwordEncoder).encode(any());

        assertThat(result, is(existingUser));
    }

    @Test
    void test_desactive_user_with_id_not_found() {
        UUID id = UUID.randomUUID();

        when(userRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.desactiveUser(id)
        );

        verify(userRepository).findById(id);
    }

    @Test
    void test_desactive_user_success() {
        User user = UserMother.userBase();
        UUID id = user.getId();

        when(userRepository.findById(id))
                .thenReturn(Optional.of(user));

        userService.desactiveUser(id);

        verify(userRepository).findById(id);
        verify(userRepository).save(user);
    }

    @Test
    void test_activate_user_with_id_not_found() {
        UUID id = UUID.randomUUID();

        when(userRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.activateUser(id)
        );

        verify(userRepository).findById(id);
    }

    @Test
    void test_activate_user_success() {
        User user = UserMother.userInactive();
        UUID id = user.getId();

        when(userRepository.findById(id))
                .thenReturn(Optional.of(user));

        userService.activateUser(id);

        verify(userRepository).findById(id);
        verify(userRepository).save(user);
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
