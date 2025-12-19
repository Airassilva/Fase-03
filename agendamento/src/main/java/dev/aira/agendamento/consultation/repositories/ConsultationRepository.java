package dev.aira.agendamento.consultation.repositories;

import dev.aira.agendamento.consultation.entities.Consultation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.UUID;

public interface ConsultationRepository extends MongoRepository<Consultation, UUID> {
    @Query("""
            {
              doctor_id: ?0,
              consultationDate: {
                $lt: ?2,
                $gte: ?1
              }
            }
            """)
    boolean existsDoctorConflict(UUID doctorId, LocalDateTime start, LocalDateTime end);

    @Query("""
            {
              patient_id: ?0,
              consultationDate: {
                $lt: ?2,
                $gte: ?1
              }
            }
            """)
    boolean existsPatientConflict(UUID patientId, LocalDateTime start, LocalDateTime end);

    @Query("""
            {
              doctorId: ?0,
              consultationDate: {
                $lt: ?2,
                $gte: ?1
              },
              _id: { $ne: ?3 }
            }
           """)
    boolean existsDoctorConflictExcludingConsultation(UUID doctorId, LocalDateTime start, LocalDateTime end, UUID consultationId);
}
