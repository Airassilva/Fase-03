package dev.aira.agendamento.objectMother;

import dev.aira.agendamento.consultation.entities.Consultation;
import dev.aira.agendamento.consultation.entities.ConsultationStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class ConsultationMother {

    public static Consultation consultationBase (UUID id){
        UUID userId = UUID.randomUUID();
        return new Consultation(
                    id,
                    userId,
                    LocalDateTime.now(),
                    "dermatologist",
                    ConsultationStatus.AGENDADA,
                    "skin disease"
        );
    }
}
