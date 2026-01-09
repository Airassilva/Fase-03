package dev.aira.agendamento.user.ut.controllers;

import dev.aira.agendamento.exceptions.ExistingEmailException;
import dev.aira.agendamento.exceptions.UserInactiveException;
import dev.aira.agendamento.exceptions.UserNotFoundException;
import dev.aira.agendamento.objectMother.UserMother;
import dev.aira.agendamento.user.controller.UserController;
import dev.aira.agendamento.user.dtos.*;
import dev.aira.agendamento.user.entities.User;
import dev.aira.agendamento.user.mapper.UserMapper;
import dev.aira.agendamento.user.service.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    @Test
    void test_create_new_user() {
        UUID id = UUID.randomUUID();
        UserRequest userRequest = UserMother.userRequest();
        User user = UserMother.userBase();
        User userSaved = UserMother.userSaved(id);
        UserResponse userResponse = UserMother.userResponse(id);

        when(userMapper.toEntity(userRequest)).thenReturn(user);
        when(userService.create(user)).thenReturn(userSaved);
        when(userMapper.toResponse(userSaved)).thenReturn(userResponse);

        ResponseEntity<UserResponse> response =
                userController.createUser(userRequest);

        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(response.getBody(), notNullValue());
        assertThat(response.getBody().getId(), is(userResponse.getId()));
        assertThat(response.getBody().getName(), is(userResponse.getName()));
        assertThat(response.getBody().getEmail(), is(userResponse.getEmail()));
    }

    @Test
    void test_create_new_user_with_existing_email() {
        UUID id = UUID.randomUUID();
        UserRequest userRequest = UserMother.userRequest();
        User user = UserMother.userSaved(id);

        when(userMapper.toEntity(userRequest)).thenReturn(user);
        when(userService.create(user)).thenThrow(ExistingEmailException.class);

        assertThrows(
                ExistingEmailException.class,
                () -> userController.createUser(userRequest));
    }

    @Test
    void test_login_success() {
        LoginRequest loginRequest = UserMother.loginRequest();
        LoginResponse loginResponse = UserMother.loginResponse();

        when(userService.login(loginRequest))
                .thenReturn(loginResponse);

        ResponseEntity<LoginResponse> response =
                userController.login(loginRequest);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), notNullValue());
        assertThat(response.getBody().accessToken(), is(loginResponse.accessToken()));
        assertThat(response.getBody().expiresIn(), is(loginResponse.expiresIn()));
    }

    @Test
    void test_login_invalid_credentials() {
        LoginRequest loginRequest = UserMother.loginRequest();

        when(userService.login(loginRequest))
                .thenThrow(BadCredentialsException.class);

        assertThrows(
                BadCredentialsException.class,
                () -> userController.login(loginRequest)
        );
    }

    @Test
    void test_update_user(){
        UUID id = UUID.randomUUID();
        UserUpdateRequest userUpdateRequest = UserMother.userUpdateRequest();
        User userSaved = UserMother.userSaved(id);
        UserResponse userResponse = UserMother.userResponse(id);

        when(userService.update(eq(id), any(UserUpdateRequest.class)))
                .thenReturn(userSaved);
        when(userMapper.toResponse(userSaved)).thenReturn(userResponse);

        ResponseEntity<UserResponse> response =
                userController.update(userUpdateRequest, id);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), notNullValue());
        assertThat(response.getBody().getId(), is(id));
        assertThat(response.getBody().getEmail(), is(userSaved.getEmail()));
    }

    @Test
    void test_update_user_inactive(){
        UUID id = UUID.randomUUID();
        UserUpdateRequest userUpdateRequest = UserMother.userUpdateRequest();

        when(userService.update(id, userUpdateRequest))
                .thenThrow(UserInactiveException.class);

        assertThrows(UserInactiveException.class,
                () -> userController.update(userUpdateRequest, id));
    }

    @Test
    void test_update_user_with_user_id_not_found(){
        UUID id = UUID.randomUUID();
        UserUpdateRequest userUpdateRequest = UserMother.userUpdateRequest();

        when(userService.update(id, userUpdateRequest))
                .thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class,
                () -> userController.update(userUpdateRequest, id));
    }

    @Test
    void test_get_find_by_id(){
        UUID id = UUID.randomUUID();
        User user = UserMother.userSaved(id);
        UserResponse userResponse = UserMother.userResponse(id);

        when(userService.findById(id)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponse);
        ResponseEntity<UserResponse> response = userController.findById(id);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), notNullValue());
        assertThat(response.getBody().getId(), is(id));
    }

    @Test
    void test_get_find_by_id_user_not_found(){
        UUID id = UUID.randomUUID();

        when(userService.findById(id)).thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class,
                () -> userController.findById(id));
    }

    @Test
    void test_get_find_all(){
        UUID id  = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserResponse> responsePage =
                new PageImpl<>(List.of(UserMother.userResponse(id)));

        when(userService.findAll(pageable)).thenReturn(Page.empty());
        when(userMapper.pageEntityToPageResponse(any()))
                .thenReturn(responsePage);
        ResponseEntity<Page<UserResponse>> response =
                userController.findAll(pageable);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), notNullValue());
    }

    @Test
    void test_desactivate_user(){
        UUID id = UUID.randomUUID();
        doNothing().when(userService).desactiveUser(id);
        ResponseEntity<Void> response = userController.deactivateUser(id);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService).desactiveUser(id);
    }

    @Test
    void test_desactivate_user_not_found(){
        UUID id = UUID.randomUUID();
        doThrow(UserNotFoundException.class).when(userService).desactiveUser(id);
        assertThrows(UserNotFoundException.class,
                () -> userController.deactivateUser(id));
        verify(userService).desactiveUser(id);
    }

    @Test
    void test_activate_user(){
        UUID id = UUID.randomUUID();
        doNothing().when(userService).activateUser(id);
        ResponseEntity<Void> response = userController.activateUser(id);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService).activateUser(id);
    }

    @Test
    void test_activate_user_not_found(){
        UUID id = UUID.randomUUID();
        doThrow(UserNotFoundException.class).when(userService).activateUser(id);
        assertThrows(UserNotFoundException.class,
                () -> userController.activateUser(id));
        verify(userService).activateUser(id);
    }

    @Test
    void test_delete_user(){
        UUID id = UUID.randomUUID();

        doNothing().when(userService).delete(id);
        ResponseEntity<Void> response = userController.delete(id);

        assertThat(response.getStatusCode(), is(HttpStatus.NO_CONTENT));
        verify(userService).delete(id);
    }

    @Test
    void test_delete_user_with_user_id_not_found(){
        UUID id = UUID.randomUUID();

        doThrow(UserNotFoundException.class)
                .when(userService).delete(id);

        assertThrows(
                UserNotFoundException.class,
                () -> userController.delete(id)
        );

        verify(userService).delete(id);
    }
}
