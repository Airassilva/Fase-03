package dev.aira.historico.controllers;

import dev.aira.historico.dtos.ConsultationDTO;
import dev.aira.historico.dtos.UpdateConsultationInputDTO;
import dev.aira.historico.enums.ConsultationStatus;
import dev.aira.historico.services.ConsultationService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ConsultationQueryController {
    private final ConsultationService  consultationService;

    @QueryMapping
    @PreAuthorize("hasRole('PACIENTE')")
    public List<ConsultationDTO> myConsultations(@Argument ConsultationStatus status, @Argument String from, @Argument String to, @Argument Boolean onlyFuture) {
        return consultationService.myConsultations(status, from, to, onlyFuture);
    }

    @QueryMapping
    @PreAuthorize("hasAnyRole('MEDICO', 'ENFERMEIRO')")
    public List<ConsultationDTO> patientConsultations(@Argument UUID patientId, @Argument ConsultationStatus status, @Argument String from, @Argument String to, @Argument Boolean onlyFuture) {
        return consultationService.patientConsultations(patientId, status, from, to, onlyFuture);
    }

    @QueryMapping
    @PreAuthorize("hasAnyRole('MEDICO', 'ENFERMEIRO')")
    public List<ConsultationDTO> allConsultations(@Argument ConsultationStatus status, @Argument String from, @Argument String to, @Argument Boolean onlyFuture){
        return consultationService.allConsultations(status, from, to, onlyFuture);
    }

    @MutationMapping
    @PreAuthorize("hasRole('MEDICO')")
    public ConsultationDTO updateConsultation(@Argument UpdateConsultationInputDTO input) {
        return consultationService.update(input);
    }
}

