package dev.aira.agendamento.consultation.repositories;

import dev.aira.agendamento.consultation.entities.Consultation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface ConsultationRepository extends MongoRepository<Consultation, UUID> {
    boolean existsByDoctorIdAndConsultationDateBetween(
            UUID doctorId,
            LocalDateTime start,
            LocalDateTime end
    );

    boolean existsByPatientIdAndConsultationDateBetween(
            UUID patientId,
            LocalDateTime start,
            LocalDateTime end
    );

    boolean existsByDoctorIdAndConsultationDateBetweenAndIdNot(
            UUID doctorId,
            LocalDateTime start,
            LocalDateTime end,
            UUID consultationId
    );
}
