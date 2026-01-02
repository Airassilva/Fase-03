package dev.aira.notificacao.consumer;

import com.google.protobuf.InvalidProtocolBufferException;
import dev.aira.notificacao.ConsultationEvents;
import dev.aira.notificacao.exceptions.ConsultationConsumerException;
import dev.aira.notificacao.service.NotificationService;
import dev.aira.notificacao.service.ReminderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConsultationConsumer {

    private final NotificationService notificationService;
    private final ReminderService reminderService;

    @KafkaListener(topics = "consultation-notification",
                   groupId = "notificacao-consultation-group",
                   containerFactory = "protobufKafkaListenerContainerFactory",
                   autoStartup = "true" )
    public void consume(byte[] message) {
        try {
            log.info("ConsultationConsumer received : {}", message);

            ConsultationEvents.ConsultationCreated event =
                    ConsultationEvents.ConsultationCreated.parseFrom(message);

            log.info("Evento recebido {}", event);

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
