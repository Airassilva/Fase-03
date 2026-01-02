package dev.aira.notificacao.consumer;

import dev.aira.notificacao.dtos.ReminderEvent;
import dev.aira.notificacao.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReminderConsumer {
    private final NotificationService notificationService;

    @KafkaListener(topics = "reminder-events",
                   groupId = "notificacao-reminder-group",
                   containerFactory = "jsonKafkaListenerContainerFactory")
    public void onReminder(ReminderEvent event) {
        log.info("Reminder received: {}", event);
        notificationService.notifyReminder(event);
    }
}
