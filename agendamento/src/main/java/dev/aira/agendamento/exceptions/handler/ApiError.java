package dev.aira.agendamento.exceptions.handler;

import java.time.LocalDateTime;

public record ApiError(
        Object mensagem,
        LocalDateTime timestamp
) {
    public ApiError(Object mensagem) {
        this(mensagem, LocalDateTime.now());
    }
}

