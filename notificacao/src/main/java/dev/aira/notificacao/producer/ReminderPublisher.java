package dev.aira.notificacao.producer;

import dev.aira.notificacao.dtos.ReminderEvent;
import dev.aira.notificacao.entities.Reminder;
import dev.aira.notificacao.repositories.ReminderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReminderPublisher {

    private final ReminderRepository reminderRepository;
    private final KafkaTemplate<String, ReminderEvent> kafkaTemplate;

    @Scheduled(fixedRate = 600_000)
    public void publishPendingReminders() {

        List<Reminder> reminders =
                reminderRepository.findPendingForNow(LocalDateTime.now());

        reminders.forEach(reminder -> {
            ReminderEvent event = new ReminderEvent(
                    reminder.getConsultationId(),
                    reminder.getUserId(),
                    reminder.getType()
            );

            kafkaTemplate.send(
                    "reminder-events",
                    reminder.getConsultationId().toString(),
                    event
            );

            reminder.markAsSent();
            reminderRepository.save(reminder);
        });
    }
}
