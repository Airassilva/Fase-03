package dev.aira.agendamento.consultation.dtos;

import dev.aira.agendamento.consultation.entities.ConsultationStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ConsultationResponse {
    private UUID doctorId;
    private String specialty;
    private ConsultationStatus status;
    private LocalDateTime consultationDate;
}
