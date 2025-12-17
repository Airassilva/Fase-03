package dev.aira.agendamento.user.validations;

import dev.aira.agendamento.exceptions.UserInactiveException;
import dev.aira.agendamento.user.entities.User;
import org.springframework.stereotype.Component;

@Component
public class ActiveUserValidation implements UserUpdateValidation{

    @Override
    public void validation(User user) {
        if (user.isActive()) {
            throw new UserInactiveException(user.getId());
        }
    }
}
