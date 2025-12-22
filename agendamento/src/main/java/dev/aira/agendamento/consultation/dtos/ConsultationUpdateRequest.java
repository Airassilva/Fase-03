package dev.aira.agendamento.consultation.dtos;

import jakarta.validation.constraints.Future;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ConsultationUpdateRequest {
    private UUID doctorId;
    private String specialty;
    @Future(message = "The date of the consultation must be in the future!")
    private LocalDateTime consultationDate;
}
