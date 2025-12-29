package dev.aira.agendamento.consultation.entities;

import dev.aira.agendamento.consultation.dtos.ConsultationUpdateRequest;
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

    public void addId(){
        this.id = UUID.randomUUID();
    }

    public void updateStatus(ConsultationStatus status) {
        this.status = status;
    }

    public void consultationUpdate(ConsultationUpdateRequest dto) {
        if (dto.getDoctorId() != null && !dto.getDoctorId().equals(this.doctorId)) {
            this.doctorId = dto.getDoctorId();
        }
        if (dto.getSpecialty() != null && !dto.getSpecialty().equals(this.specialty)) {
            this.specialty = dto.getSpecialty();
        }
        if (dto.getConsultationDate() != null && !dto.getConsultationDate().equals(this.consultationDate)) {
            this.consultationDate = dto.getConsultationDate();
        }
    }

    public Consultation(UUID doctorId, UUID patientId, LocalDateTime consultationDate, String specialty, String observation) {
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.consultationDate = consultationDate;
        this.specialty = specialty;
        this.observation = observation;
    }
}

