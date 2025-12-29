package dev.aira.notificacao.consumer;

import com.google.protobuf.InvalidProtocolBufferException;
import dev.aira.notificacao.ConsultationEvents;
import dev.aira.notificacao.exceptions.ConsultationConsumerException;
import dev.aira.notificacao.service.NotificationService;
import dev.aira.notificacao.service.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConsultationConsumer {

    private final NotificationService notificationService;
    private final ReminderService reminderService;

    @KafkaListener(topics = "consultation-notification",
                   groupId = "notificacao-group",
                   containerFactory = "protobufKafkaListenerContainerFactory")
    public void consume(byte[] message) {
        try {
            ConsultationEvents.ConsultationCreated event =
                    ConsultationEvents.ConsultationCreated.parseFrom(message);

            notificationService.notifyByEmail(event);
            reminderService.createReminder(event);

        } catch (InvalidProtocolBufferException e) {
            throw new ConsultationConsumerException(
                    "Erro ao desserializar evento", e
            );

        } catch (Exception e) {
            throw new ConsultationConsumerException(
                    "Erro ao processar evento", e
            );
        }
    }
}
