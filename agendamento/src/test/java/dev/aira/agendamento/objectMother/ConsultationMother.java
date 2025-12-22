package dev.aira.agendamento.objectMother;

import dev.aira.agendamento.consultation.dtos.ConsultationUpdateRequest;
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
                    ConsultationStatus.PENDENTE,
                    "skin disease"
        );
    }

    public static Consultation consultationBaseStatusFinalized (){
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        return new Consultation(
                id,
                userId,
                LocalDateTime.now(),
                "dermatologist",
                ConsultationStatus.FINALIZADA,
                "skin disease"
        );
    }

    public static Consultation consultationBaseStatusCancelled (){
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        return new Consultation(
                id,
                userId,
                LocalDateTime.now(),
                "dermatologist",
                ConsultationStatus.CANCELADA,
                "skin disease"
        );
    }

    public static Consultation consultatioWithTime (LocalDateTime time){
        UUID userId = UUID.randomUUID();
        UUID doctorId = UUID.randomUUID();
        return new Consultation(
                doctorId,
                userId,
                time,
                "dermatologist",
                ConsultationStatus.AGENDADA,
                "skin disease"
        );
    }

    public static ConsultationUpdateRequest updateRequest (){
        UUID doctorId = UUID.randomUUID();
        return new ConsultationUpdateRequest(
                    doctorId,
                "ophthalmologist",
                    LocalDateTime.now().plusDays(2)
        );
    }

    public static ConsultationUpdateRequest updateRequestWithTime (LocalDateTime time){
        UUID doctorId = UUID.randomUUID();
        return new ConsultationUpdateRequest(
                    doctorId,
                    "ophthalmologist",
                    time
        );
    }
}
