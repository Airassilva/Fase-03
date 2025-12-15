package dev.aira.agendamento.consultation.entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Document(collection = "consultas")
public class Consultation {

    @Id
    private UUID id;

    @NotBlank
    private UUID doctorId;
    @NotBlank
    private UUID patientId;
    @NotBlank
    private LocalDateTime consultationDate;

    @NotBlank
    private String specialty;

    @NotNull
    private ConsultationStatus status;

    private String observation;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime updatedDate;
}

