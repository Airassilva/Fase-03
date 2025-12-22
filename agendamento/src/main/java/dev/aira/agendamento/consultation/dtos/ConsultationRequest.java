package dev.aira.agendamento.consultation.dtos;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ConsultationRequest {
    @NotBlank(message = "Doctor is required!")
    private UUID doctorId;

    @NotBlank(message = "Patient is required!")
    private UUID patientId;

    @NotBlank(message = "Consultation date is required!")
    @Future(message = "The date of the consultation must be in the future!")
    private LocalDateTime consultationDate;

    @NotBlank(message = "Specialty is required!")
    private String specialty;
    private String observation;
}
