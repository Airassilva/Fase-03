package dev.aira.notificacao.service;

import dev.aira.notificacao.enums.ConsultationStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReminderCalculatorService {

    public LocalDateTime calcular(ConsultationStatus status, LocalDateTime consultationDate) {
        return switch (status) {
            case PENDENTE ->
                    consultationDate.minusDays(2);

            case AGENDADA ->
                    consultationDate.minusHours(2);
            default -> null;
        };
    }
}
