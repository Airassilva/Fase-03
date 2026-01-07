package dev.aira.historico.services;

import dev.aira.historico.ConsultationEvents.ConsultationCreated;
import dev.aira.historico.entities.Consultation;
import dev.aira.historico.enums.ConsultationStatus;
import dev.aira.historico.repositories.ConsultationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConsumerService {
    private final ConsultationRepository consultationRepository;

    public void saveConsultation(ConsultationCreated event) {
        UUID consultationId = UUID.fromString(event.getConsultationId());
        UUID patientId = UUID.fromString(event.getUserId());
        UUID doctorId = UUID.fromString(event.getDoctorId());
        ConsultationStatus status;
        try {
            status = ConsultationStatus.valueOf(event.getConsultationStatus());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Status inválido: " + event.getConsultationStatus()
            );
        }
        LocalDateTime date = LocalDateTime.parse(event.getConsultationDate());
        Consultation consultation = new Consultation(
                consultationId,
                patientId,
                doctorId,
                date,
                status
        );
        consultationRepository.save(consultation);
    }
}
