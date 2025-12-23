package dev.aira.agendamento.consultation.service;

import dev.aira.agendamento.consultation.dtos.ConsultationUpdateRequest;
import dev.aira.agendamento.consultation.entities.Consultation;
import dev.aira.agendamento.consultation.entities.ConsultationStatus;
import dev.aira.agendamento.consultation.repositories.ConsultationRepository;
import dev.aira.agendamento.consultation.validations.ConsultationCreateValidation;
import dev.aira.agendamento.consultation.validations.ConsultationUpdateValidation;
import dev.aira.agendamento.exceptions.ConsultationNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConsultationService {
    private final ConsultationRepository consultationRepository;
    private final List<ConsultationCreateValidation> consultationCreateValidations;
    private final List<ConsultationUpdateValidation> consultationUpdateValidation;

    public Consultation create(Consultation consultation){
        consultationCreateValidations.forEach(v -> v.validation(consultation));
        consultation.updateStatus(ConsultationStatus.PENDENTE);
        return consultationRepository.save(consultation);
    }

    public Consultation update(UUID consultationId, ConsultationUpdateRequest consultationUpdateRequest) {
        Consultation consultation = consultationRepository.findById(consultationId).orElseThrow(ConsultationNotFoundException::new);
        consultation.consultationUpdate(consultationUpdateRequest);
        consultationUpdateValidation.forEach(v -> v.validation(consultation));
        return consultationRepository.save(consultation);
    }

    private Consultation updateStatus(UUID consultationId, ConsultationStatus status) {
        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(ConsultationNotFoundException::new);
        consultation.updateStatus(status);
        return consultationRepository.save(consultation);
    }

    public Consultation confirmConsultation(UUID consultationId){
        return updateStatus(consultationId, ConsultationStatus.AGENDADA);
    }

    public Consultation cancelConsultation (UUID consultationId) {
        return updateStatus(consultationId, ConsultationStatus.CANCELADA);
    }

    public Consultation finishConsultation(UUID consultationId) {
        return updateStatus(consultationId, ConsultationStatus.FINALIZADA);
    }
}
