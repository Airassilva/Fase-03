package dev.aira.historico.repositories;

import dev.aira.historico.entities.Consultation;
import dev.aira.historico.enums.ConsultationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, UUID> {
    @Query("""
        select c from consultation c
        where c.patientId = :patientId
          and (:status is null or c.status = :status)
          and (:from is null or c.consultationDate >= :from)
          and (:to is null or c.consultationDate <= :to)
          and (:onlyFuture = false or c.consultationDate >= current_timestamp)
    """)
    List<Consultation> findPatientConsultations(UUID patientId, ConsultationStatus status, LocalDateTime from, LocalDateTime to, Boolean onlyFuture);
}
