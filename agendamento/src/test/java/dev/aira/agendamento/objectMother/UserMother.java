package dev.aira.agendamento.objectMother;

import dev.aira.agendamento.user.dtos.*;
import dev.aira.agendamento.user.entities.UserType;
import dev.aira.agendamento.user.entities.User;

import java.util.UUID;

public class UserMother {
    public static User userBase() {
       return new User("teste@email.com", "1234", "teste", UserType.PACIENTE);
    }

    public static User userInactive(){
        User base = userBase();
        base.deactivate();
        return base;
    }

    public static User userSaved(UUID id) {
        User base = userBase();
        return new User(
                id,
                base.getEmail(),
                base.getPassword(),
                base.getName(),
                base.getUserType()
        );
    }

    public static User userSavedDoctor(UUID id) {
        User base = userBase();
        return new User(
                id,
                base.getEmail(),
                base.getPassword(),
                base.getName(),
                UserType.MEDICO
        );
    }

    public static UserResponse userResponse(UUID id) {
        User user = userSaved(id);
        return new UserResponse(user.getId(),
                                user.getName(),
                                user.getEmail(),
                                user.getUserType());
    }

    public static User userExisting() {
        return new User("teste@email.com", "senha", "teste", UserType.MEDICO);
    }

    public static UserUpdateRequest  userUpdateRequest() {
        return new UserUpdateRequest("teste@email.com", "1234");
    }

    public static UserRequest  userRequest() {
        return new UserRequest("teste","teste@email.com", "1234", UserType.MEDICO);
    }

    public static LoginRequest loginRequest() {
        return new LoginRequest("larisa@email.com", "1234");
    }

    public static LoginResponse loginResponse() {
        return new LoginResponse("jwt-acess-token", 300L);
    }
}
