package dev.aira.agendamento.consultation.entities;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Document(collection = "consultas")
public class Consultation {

    @Id
    private UUID id;

    @NotBlank(message = "Doctor is required!")
    @Field("doctor_id")
    private UUID doctorId;

    @NotBlank(message = "Patient is required!")
    @Field("patient_id")
    private UUID patientId;

    @NotBlank(message = "Consultation date is required!")
    @Future(message = "The date of the consultation must be in the future!")
    private LocalDateTime consultationDate;

    @NotBlank(message = "Specialty is required!")
    private String specialty;

    @NotNull
    private ConsultationStatus status;

    private String observation;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime updatedDate;

    public Consultation(UUID doctorId, UUID patientId, LocalDateTime consultationDate, String specialty, ConsultationStatus status, String observation) {
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.consultationDate = consultationDate;
        this.specialty = specialty;
        this.status = status;
        this.observation = observation;
    }
}

