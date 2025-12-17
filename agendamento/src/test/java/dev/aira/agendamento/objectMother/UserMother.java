package dev.aira.agendamento.objectMother;

import dev.aira.agendamento.user.dtos.UserRequest;
import dev.aira.agendamento.user.dtos.UserResponse;
import dev.aira.agendamento.user.dtos.UserUpdateRequest;
import dev.aira.agendamento.user.entities.UserType;
import dev.aira.agendamento.user.entities.User;

import java.util.UUID;

public class UserMother {
    public static User userBase() {
       return new User("teste@email.com", "1234", "teste", UserType.MEDICO);
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
}
