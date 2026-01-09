package dev.aira.notificacao.service;

import dev.aira.notificacao.ConsultationEvents.ConsultationCreated;
import dev.aira.notificacao.entities.Reminder;
import dev.aira.notificacao.enums.ConsultationStatus;
import dev.aira.notificacao.enums.ReminderType;
import dev.aira.notificacao.repositories.ReminderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReminderService {

    private final ReminderRepository repository;
    private final ReminderCalculatorService calculator;

    @Transactional
    public void createReminder(ConsultationCreated event) {
        log.info("Evento recebido {}", event);

        ConsultationStatus status = ConsultationStatus.valueOf(event.getConsultationStatus());
        UUID userId = UUID.fromString(event.getUserId());
        UUID consultationId = UUID.fromString(event.getConsultationId());
        LocalDateTime consultationDate = LocalDateTime.parse(event.getConsultationDate());

        LocalDateTime schedule =
                calculator.calcular(
                        status,
                        consultationDate
                );
        log.info("schedule {}", schedule);
        if (schedule == null || schedule.isBefore(LocalDateTime.now())) {
            return;
        }

        Reminder reminder = new Reminder(
                consultationId,
                userId,
                setType(status),
                schedule,
                false
        );
        log.info("Salvando reminder {}", reminder);
        repository.save(reminder);
    }

    private ReminderType setType(ConsultationStatus status) {
        return status == ConsultationStatus.PENDENTE
                ? ReminderType.CONFIRMATION
                : ReminderType.UPCOMING;
    }

}
