package dev.aira.historico.repositories;

import dev.aira.historico.entities.Consultation;
import dev.aira.historico.enums.ConsultationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, UUID> {
    @Query
    ("""
     select c from Consultation c
     where c.patientId = :patientId
      and (:status is null or c.status = :status)
      and (cast(:from as timestamp) is null or c.consultationDate >= :from)
      and (cast(:to as timestamp) is null or c.consultationDate <= :to)
      and (:onlyFuture = false or c.consultationDate >= current_timestamp)
    """)
    List<Consultation> findPatientConsultations(@Param("patientId") UUID patientId, @Param("status") ConsultationStatus status, @Param("from") LocalDateTime from, @Param("to") LocalDateTime to, @Param("onlyFuture") boolean onlyFuture);

    @Query
    ("""
     select c from Consultation c
      where (:status is null or c.status = :status)
      and (cast(:from as timestamp) is null or c.consultationDate >= :from)
      and (cast(:to as timestamp) is null or c.consultationDate <= :to)
      and (:onlyFuture = false or c.consultationDate >= current_timestamp)
    """)
    List<Consultation> findAll (@Param("status") ConsultationStatus status, @Param("from") LocalDateTime from, @Param("to") LocalDateTime to, @Param("onlyFuture") boolean onlyFuture);
}
