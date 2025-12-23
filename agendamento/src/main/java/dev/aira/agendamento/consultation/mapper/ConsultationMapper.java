package dev.aira.agendamento.consultation.mapper;

import dev.aira.agendamento.consultation.dtos.ConsultationRequest;
import dev.aira.agendamento.consultation.dtos.ConsultationResponse;
import dev.aira.agendamento.consultation.entities.Consultation;
import org.springframework.stereotype.Component;

@Component
public class ConsultationMapper {

    public Consultation toEntity(ConsultationRequest consultationRequest) {
        return new Consultation(
                        consultationRequest.getDoctorId(),
                        consultationRequest.getPatientId(),
                        consultationRequest.getConsultationDate(),
                        consultationRequest.getSpecialty(),
                        consultationRequest.getObservation()
        );
    }

    public ConsultationResponse  toResponse(Consultation consultation) {
        return new ConsultationResponse(
                    consultation.getDoctorId(),
                    consultation.getSpecialty(),
                    consultation.getStatus(),
                    consultation.getConsultationDate()
        );
    }
}
