package dev.aira.notificacao.exceptions;

import java.util.UUID;

public class StatusUserNotFoundException extends NotFoundException {
    public StatusUserNotFoundException(UUID  id) {
        super("Status User Not Found with id: " + id);
    }
}
