package dev.aira.agendamento.consultation.controller;

import dev.aira.agendamento.consultation.dtos.ConsultationRequest;
import dev.aira.agendamento.consultation.dtos.ConsultationResponse;
import dev.aira.agendamento.consultation.dtos.ConsultationUpdateRequest;
import dev.aira.agendamento.consultation.entities.Consultation;
import dev.aira.agendamento.consultation.mapper.ConsultationMapper;
import dev.aira.agendamento.consultation.service.ConsultationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/consultations")
@RequiredArgsConstructor
public class ConsultationController {
    private final ConsultationService consultationService;
    private final ConsultationMapper  consultationMapper;

    @PostMapping
    public ResponseEntity<ConsultationResponse> createConsultation(@Valid @RequestBody ConsultationRequest request) {
        Consultation consultationEntity = consultationMapper.toEntity(request);
        Consultation consultationSave = consultationService.create(consultationEntity);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(consultationMapper.toResponse(consultationSave));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsultationResponse> updateConsultation(@PathVariable UUID id, @Valid @RequestBody ConsultationUpdateRequest updateRequest) {
        Consultation consultationUpdate = consultationService.update(id, updateRequest);
        return ResponseEntity.ok(consultationMapper.toResponse(consultationUpdate));
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<Void> confirmConsultation(@PathVariable UUID id) {
        consultationService.confirmConsultation(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/finish")
    public ResponseEntity<Void> finishConsultation(@PathVariable UUID id) {
        consultationService.finishConsultation(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelConsultation(@PathVariable UUID id) {
        consultationService.cancelConsultation(id);
        return ResponseEntity.noContent().build();
    }
}
