package dev.aira.agendamento.message;

import dev.aira.agendamento.consultation.ConsultationEvents;
import dev.aira.agendamento.consultation.entities.Consultation;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ConsultationProducer {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public ConsultationProducer(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(Consultation consultation) {
        byte[] payload =
                ConsultationEvents.ConsultationCreated.newBuilder()
                        .setConsultationId(consultation.getId().toString())
                        .setUserId(consultation.getPatientId().toString())
                        .setConsultationDate(consultation.getConsultationDate().toString())
                        .setConsultationStatus(consultation.getStatus().toString())
                        .build()
                        .toByteArray();
        kafkaTemplate.send("consultation-notification", payload);
    }
}

