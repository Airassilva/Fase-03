package dev.aira.agendamento.exceptions;

import java.util.UUID;

public class UserInactiveException extends InactiveException {
    public UserInactiveException(UUID id) {
        super("User inactive: " + id);
    }
}
