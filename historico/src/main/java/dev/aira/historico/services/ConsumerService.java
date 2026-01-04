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
    private final ConsultationRepository  consultationRepository;

    public void saveConsultation(ConsultationCreated event){
        Consultation consultation = new Consultation(
                UUID.fromString(event.getConsultationId()),
                UUID.fromString(event.getUserId()),
                UUID.fromString(event.getDoctorId()),
                LocalDateTime.parse(event.getConsultationDate()),
                ConsultationStatus.valueOf(event.getConsultationStatus())
        );
        consultationRepository.save(consultation);
    }
}
