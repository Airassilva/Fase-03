package dev.aira.agendamento.user.ut.controllers;

import dev.aira.agendamento.consultation.controller.ConsultationController;
import dev.aira.agendamento.consultation.dtos.ConsultationRequest;
import dev.aira.agendamento.consultation.dtos.ConsultationResponse;
import dev.aira.agendamento.consultation.dtos.ConsultationUpdateRequest;
import dev.aira.agendamento.consultation.entities.Consultation;
import dev.aira.agendamento.consultation.mapper.ConsultationMapper;
import dev.aira.agendamento.consultation.service.ConsultationService;
import dev.aira.agendamento.exceptions.ConsultationAlreadyFinalizedCancelledException;
import dev.aira.agendamento.exceptions.ConsultationNotFoundException;
import dev.aira.agendamento.exceptions.InvalidDoctorUserException;
import dev.aira.agendamento.objectMother.ConsultationMother;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultationControllerTest {

    @Mock
    private ConsultationService consultationService;

    @Mock
    private ConsultationMapper consultationMapper;

    @InjectMocks
    private ConsultationController consultationController;

    @Test
    void test_create_new_consultation() {
        ConsultationRequest consultationRequest = ConsultationMother.consultationRequest();
        Consultation consultation = ConsultationMother.consultation();
        ConsultationResponse consultationResponse = ConsultationMother.consultationResponse();

        when(consultationMapper.toEntity(consultationRequest)).thenReturn(consultation);
        when(consultationService.create(consultation)).thenReturn(consultation);
        when(consultationMapper.toResponse(consultation)).thenReturn(consultationResponse);

        ResponseEntity<ConsultationResponse> response =  consultationController.createConsultation(consultationRequest);

        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(response.getBody(), notNullValue());
        assertThat(response.getBody().getDoctorId(), is(consultationResponse.getDoctorId()));

        verify(consultationMapper).toEntity(consultationRequest);
        verify(consultationService).create(consultation);
        verify(consultationMapper).toResponse(consultation);
    }

    @Test
    void test_create_new_consultation_invalid_doctor(){
        ConsultationRequest consultationRequest = ConsultationMother.consultationRequest();
        Consultation consultation = ConsultationMother.consultation();

        when(consultationMapper.toEntity(consultationRequest)).thenReturn(consultation);
        when(consultationService.create(consultation)).thenThrow(InvalidDoctorUserException.class);

        assertThrows(
                InvalidDoctorUserException.class,
                () -> consultationController.createConsultation(consultationRequest)
        );
        verify(consultationMapper).toEntity(consultationRequest);
        verify(consultationService).create(consultation);
    }

    @Test
    void test_update_consultation_valid(){
        ConsultationUpdateRequest updateRequest = ConsultationMother.updateRequest();
        Consultation consultation = ConsultationMother.consultation();
        ConsultationResponse consultationResponse = ConsultationMother.consultationResponse();
        UUID id = consultation.getId();

        when(consultationService.update(id, updateRequest)).thenReturn(consultation);
        when(consultationMapper.toResponse(consultation)).thenReturn(consultationResponse);

        ResponseEntity<ConsultationResponse> response =  consultationController.updateConsultation(id, updateRequest);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), notNullValue());
        assertThat(response.getBody().getDoctorId(), is(consultationResponse.getDoctorId()));

        verify(consultationService).update(id, updateRequest);
        verify(consultationMapper).toResponse(consultation);
    }

    @Test
    void test_update_consultation_status_invalid(){
        Consultation consultationCancelled = ConsultationMother.consultationBaseStatusCancelled();
        ConsultationUpdateRequest updateRequest = ConsultationMother.updateRequest();
        UUID id = consultationCancelled.getId();

        doThrow(ConsultationAlreadyFinalizedCancelledException.class)
                .when(consultationService).update(id, updateRequest);

        assertThrows(
                ConsultationAlreadyFinalizedCancelledException.class,
                () -> consultationController.updateConsultation(id, updateRequest)
        );
        verify(consultationService).update(id, updateRequest);
    }

    @Test
    void test_confirm_consultation(){
        UUID id = UUID.randomUUID();
        consultationController.confirmConsultation(id);
        verify(consultationService).confirmConsultation(id);
    }

    @Test
    void test_cancel_consultation(){
        UUID id = UUID.randomUUID();
        consultationController.cancelConsultation(id);
        verify(consultationService).cancelConsultation(id);
    }

    @Test
    void test_cancel_consultation_not_found(){
        UUID id = UUID.randomUUID();

        doThrow(ConsultationNotFoundException.class)
                .when(consultationService).cancelConsultation(id);

        assertThrows(
                ConsultationNotFoundException.class,
                () -> consultationController.cancelConsultation(id)
        );
        verify(consultationService).cancelConsultation(id);
    }

    @Test
    void test_finalize_consultation(){
        UUID id = UUID.randomUUID();
        consultationController.finishConsultation(id);
        verify(consultationService).finishConsultation(id);
    }

}
