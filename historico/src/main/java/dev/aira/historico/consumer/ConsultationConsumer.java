package dev.aira.historico.consumer;

import com.google.protobuf.InvalidProtocolBufferException;
import dev.aira.historico.ConsultationEvents.ConsultationCreated;
import dev.aira.historico.exceptions.ConsultationConsumerException;
import dev.aira.historico.services.ConsumerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ConsultationConsumer {

    private final ConsumerService consumerService;

    @KafkaListener(topics = "consultation-notification",
                   groupId = "historico-consultation-group",
                   containerFactory = "protobufKafkaListenerContainerFactory",
                   autoStartup = "true" )
    public void consume(byte[] message) {
        try {
            log.info("ConsultationConsumer received : {}", message);

            ConsultationCreated event = ConsultationCreated.parseFrom(message);

            log.info("Evento recebido {}", event);

            consumerService.saveConsultation(event);

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
