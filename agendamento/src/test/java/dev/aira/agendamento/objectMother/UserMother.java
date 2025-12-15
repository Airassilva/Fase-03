package dev.aira.agendamento.objectMother;

import dev.aira.agendamento.user.entities.TypeUser;
import dev.aira.agendamento.user.entities.User;

public class UserMother {
    public static User userBase() {
       return new User("teste@email.com", "1234", "teste", TypeUser.MEDICO);
    }

    public static User userExisting() {
        return new User("teste@email.com", "senha", "teste", TypeUser.MEDICO);
    }
}
