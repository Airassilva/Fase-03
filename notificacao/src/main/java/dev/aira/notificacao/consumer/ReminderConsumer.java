package dev.aira.notificacao.consumer;

import dev.aira.notificacao.dtos.ReminderEvent;
import dev.aira.notificacao.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReminderConsumer {
    private final NotificationService notificationService;

    @KafkaListener(topics = "reminder-events",
                   groupId = "notificacao-group",
                   containerFactory = "jsonKafkaListenerContainerFactory")
    public void onReminder(ReminderEvent event) {
        notificationService.notifyReminder(event);
    }
}
