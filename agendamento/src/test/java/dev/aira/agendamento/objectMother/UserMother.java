package dev.aira.agendamento.objectMother;

import dev.aira.agendamento.user.dtos.UserUpdateRequest;
import dev.aira.agendamento.user.entities.UserType;
import dev.aira.agendamento.user.entities.User;

public class UserMother {
    public static User userBase() {
       return new User("teste@email.com", "1234", "teste", UserType.MEDICO);
    }

    public static User userExisting() {
        return new User("teste@email.com", "senha", "teste", UserType.MEDICO);
    }

    public static UserUpdateRequest  userUpdateRequest() {
        return new UserUpdateRequest("teste@email.com", "1234");
    }
}
