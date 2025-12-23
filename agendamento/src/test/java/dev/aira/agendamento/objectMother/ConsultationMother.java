package dev.aira.agendamento.objectMother;

import dev.aira.agendamento.consultation.dtos.ConsultationRequest;
import dev.aira.agendamento.consultation.dtos.ConsultationResponse;
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
                    "skin disease"
        );
    }

    public static Consultation consultationBaseStatusFinalized (){
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Consultation consultation = new Consultation(
                id,
                userId,
                LocalDateTime.now(),
                "dermatologist",
                "skin disease"
        );
        consultation.updateStatus(ConsultationStatus.FINALIZADA);
        return consultation;
    }

    public static Consultation consultationBaseStatusCancelled (){
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Consultation consultation = new Consultation(
                id,
                userId,
                LocalDateTime.now(),
                "dermatologist",
                "skin disease"
        );
        consultation.updateStatus(ConsultationStatus.CANCELADA);
        return consultation;
    }

    public static Consultation consultatioWithTime (LocalDateTime time){
        UUID userId = UUID.randomUUID();
        UUID doctorId = UUID.randomUUID();
        return new Consultation(
                doctorId,
                userId,
                time,
                "dermatologist",
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

    public static ConsultationRequest consultationRequest (){
        UUID doctorId = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();
        return new ConsultationRequest(
                doctorId,
                patientId,
                LocalDateTime.now().plusDays(3),
                "dermatologist",
                "skin disease"
        );
    }

    public static Consultation consultation(){
        ConsultationRequest consultationRequest = consultationRequest();
        return new Consultation(
                consultationRequest.getDoctorId(),
                consultationRequest.getPatientId(),
                consultationRequest.getConsultationDate(),
                consultationRequest.getSpecialty(),
                consultationRequest.getObservation()
        );
    }

    public static ConsultationResponse consultationResponse (){
        Consultation consultation = consultation();
        return new ConsultationResponse(
                consultation.getDoctorId(),
                consultation.getSpecialty(),
                consultation.getStatus(),
                consultation.getConsultationDate()
        );
    }
}
