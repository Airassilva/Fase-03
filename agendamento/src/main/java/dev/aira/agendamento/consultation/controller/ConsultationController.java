package dev.aira.agendamento.consultation.controller;

import dev.aira.agendamento.consultation.dtos.ConsultationRequest;
import dev.aira.agendamento.consultation.dtos.ConsultationResponse;
import dev.aira.agendamento.consultation.dtos.ConsultationUpdateRequest;
import dev.aira.agendamento.consultation.entities.Consultation;
import dev.aira.agendamento.consultation.mapper.ConsultationMapper;
import dev.aira.agendamento.consultation.service.ConsultationService;
import dev.aira.agendamento.message.ConsultationProducer;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/consultations")
@RequiredArgsConstructor
public class ConsultationController {
    private final ConsultationService consultationService;
    private final ConsultationMapper  consultationMapper;
    private final ConsultationProducer consultationProducer;

    @PreAuthorize("hasRole('ENFERMEIRO')")
    @PostMapping
    public ResponseEntity<ConsultationResponse> createConsultation(@Valid @RequestBody ConsultationRequest request) {
        Consultation consultationEntity = consultationMapper.toEntity(request);
        Consultation consultationSave = consultationService.create(consultationEntity);
        consultationProducer.send(consultationSave);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(consultationMapper.toResponse(consultationSave));
    }

    @PreAuthorize("hasAnyRole('MEDICO', 'ENFERMEIRO')")
    @PutMapping("/{id}")
    public ResponseEntity<ConsultationResponse> updateConsultation(@PathVariable UUID id, @Valid @RequestBody ConsultationUpdateRequest updateRequest) {
        Consultation consultationUpdate = consultationService.update(id, updateRequest);
        consultationProducer.send(consultationUpdate);
        return ResponseEntity.ok(consultationMapper.toResponse(consultationUpdate));
    }

    @PreAuthorize("hasAnyRole('PACIENTE', 'MEDICO', 'ENFERMEIRA')")
    @PatchMapping("/{id}/confirm")
    public ResponseEntity<Void> confirmConsultation(@PathVariable UUID id) {
        consultationService.confirmConsultation(id);
        return ResponseEntity.noContent().build();
    }
    @PreAuthorize("hasAnyRole('MEDICO', 'ENFERMEIRO')")
    @PatchMapping("/{id}/finish")
    public ResponseEntity<Void> finishConsultation(@PathVariable UUID id) {
        consultationService.finishConsultation(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('PACIENTE', 'MEDICO', 'ENFERMEIRA')")
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelConsultation(@PathVariable UUID id) {
        consultationService.cancelConsultation(id);
        return ResponseEntity.noContent().build();
    }
}
