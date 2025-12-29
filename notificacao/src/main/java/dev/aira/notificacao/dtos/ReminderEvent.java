package dev.aira.notificacao.dtos;

import dev.aira.notificacao.enums.ReminderType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ReminderEvent {
    private UUID consultationId;
    private UUID userId;
    private ReminderType type;
}

