package dev.aira.agendamento.consultation.repositories;

import dev.aira.agendamento.consultation.entities.Consultation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface ConsultationRepository extends MongoRepository<Consultation, UUID> {
}
