package dev.aira.notificacao.exceptions;

import java.util.UUID;

public class EmailUserNotFoundException extends NotFoundException {
    public EmailUserNotFoundException(UUID userId) {
        super("User email not found with id: " + userId.toString());
    }
}
