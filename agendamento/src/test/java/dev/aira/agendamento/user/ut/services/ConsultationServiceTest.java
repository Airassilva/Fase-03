package dev.aira.agendamento.user.ut.services;

import dev.aira.agendamento.consultation.dtos.ConsultationUpdateRequest;
import dev.aira.agendamento.consultation.entities.Consultation;
import dev.aira.agendamento.consultation.entities.ConsultationStatus;
import dev.aira.agendamento.consultation.repositories.ConsultationRepository;
import dev.aira.agendamento.consultation.service.ConsultationService;
import dev.aira.agendamento.consultation.validations.ConsultationCreateValidation;
import dev.aira.agendamento.consultation.validations.ConsultationUpdateValidation;
import dev.aira.agendamento.exceptions.*;
import dev.aira.agendamento.objectMother.ConsultationMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultationServiceTest {

    @Mock
    private ConsultationRepository consultationRepository;

    @Mock
    private ConsultationCreateValidation  consultationCreateValidation;

    @Mock
    private ConsultationUpdateValidation consultationUpdateValidation;

    @InjectMocks
    private ConsultationService consultationService;

    @BeforeEach
    void setup() {
        consultationService = new ConsultationService(
                consultationRepository,
                List.of(consultationCreateValidation),
                List.of(consultationUpdateValidation)
        );
    }

    @Test
    void test_create_new_consultation_valid() {
        UUID id = UUID.randomUUID();
        Consultation consultation = ConsultationMother.consultationBase(id);

        when(consultationRepository.save(consultation)).thenReturn(consultation);
        Consultation result = consultationService.create(consultation);

        assertThat(result.getDoctorId(), is(consultation.getDoctorId()));
        assertThat(result.getConsultationDate(), is(consultation.getConsultationDate()));
        verify(consultationCreateValidation).validation(consultation);
    }

    @Test
    void test_create_new_consultation_invalid_time(){
        LocalDateTime invalidDateTime = LocalDateTime.now()
                .minusDays(1)
                .withHour(10)
                .withMinute(30)
                .withSecond(0)
                .withNano(0);
        Consultation consultation = ConsultationMother.consultatioWithTime(invalidDateTime);

        doThrow(InvalidConsultationTimeException.class).when(consultationCreateValidation).validation(consultation);

        assertThrows(
                InvalidConsultationTimeException.class,
                () -> consultationService.create(consultation)
        );
        verify(consultationCreateValidation).validation(consultation);
    }

    @Test
    void test_create_new_consultation_doctor_unavailable(){
        LocalDateTime validDateTime =
                LocalDateTime.now().plusDays(1)
                        .withHour(10)
                        .withMinute(0)
                        .withSecond(0)
                        .withNano(0);
        Consultation consultation = ConsultationMother.consultatioWithTime(validDateTime);

        doThrow(DoctorUnavailableBusinessException.class).when(consultationCreateValidation).validation(consultation);

        assertThrows(
                DoctorUnavailableBusinessException.class,
                () -> consultationService.create(consultation)
        );
        verify(consultationCreateValidation).validation(consultation);
    }

    @Test
    void test_create_new_consultation_patient_unavailable(){
        LocalDateTime validDateTime =
                LocalDateTime.now().plusDays(1)
                        .withHour(10)
                        .withMinute(0)
                        .withSecond(0)
                        .withNano(0);
        Consultation consultation = ConsultationMother.consultatioWithTime(validDateTime);
        doThrow(PatientUnavailableBusinessException.class).when(consultationCreateValidation).validation(consultation);

        assertThrows(
                PatientUnavailableBusinessException.class,
                () -> consultationService.create(consultation)
        );
        verify(consultationCreateValidation).validation(consultation);
    }

    @Test
    void test_create_new_consultation_invalid_doctor(){
        UUID id = UUID.randomUUID();
        Consultation consultation = ConsultationMother.consultationBase(id);

        doThrow(InvalidDoctorUserException.class).when(consultationCreateValidation).validation(consultation);

        assertThrows(
                InvalidDoctorUserException.class,
                () -> consultationService.create(consultation)
        );
        verify(consultationCreateValidation).validation(consultation);
    }

    @Test
    void test_create_new_consultation_invalid_patient(){
        UUID id = UUID.randomUUID();
        Consultation consultation = ConsultationMother.consultationBase(id);

        doThrow(InvalidPatientUserException.class).when(consultationCreateValidation).validation(consultation);

        assertThrows(
                InvalidPatientUserException.class,
                () -> consultationService.create(consultation)
        );
        verify(consultationCreateValidation).validation(consultation);
    }

    @Test
    void test_update_consultation_valid() {
        UUID id = UUID.randomUUID();
        Consultation consultation = ConsultationMother.consultationBase(id);
        ConsultationUpdateRequest updateRequest = ConsultationMother.updateRequest();
        UUID consultationId = consultation.getId();

        when(consultationRepository.findById(consultationId)).thenReturn(Optional.of(consultation));
        when(consultationRepository.save(any(Consultation.class))).thenReturn(consultation);

        Consultation result = consultationService.update(consultationId, updateRequest);

        assertThat(result, is(notNullValue()));
        assertThat(result.getDoctorId(), is(consultation.getDoctorId()));
        assertThat(result.getConsultationDate(), is(consultation.getConsultationDate()));
        verify(consultationUpdateValidation).validation(consultation);
        verify(consultationRepository).findById(consultationId);
        verify(consultationRepository).save(any(Consultation.class));
    }

    @Test
    void test_update_consultation_not_found() {
        Consultation consultation = ConsultationMother.consultationBase(UUID.randomUUID());
        ConsultationUpdateRequest updateRequest = ConsultationMother.updateRequest();
        UUID consultationId = consultation.getId();

        when(consultationRepository.findById(consultationId)).thenReturn(Optional.empty());

        assertThrows(
                ConsultationNotFoundException.class,
                () -> consultationService.update(consultationId,updateRequest)
        );
        verify(consultationRepository).findById(consultationId);
        verify(consultationRepository, never()).save(any());
    }

    @Test
    void test_update_consultation_time_invalid(){
        LocalDateTime invalidDateTime = LocalDateTime.now()
                .minusDays(1)
                .withHour(10)
                .withMinute(30)
                .withSecond(0)
                .withNano(0);
        Consultation consultation = ConsultationMother.consultatioWithTime(invalidDateTime);
        ConsultationUpdateRequest updateRequest = ConsultationMother.updateRequestWithTime(invalidDateTime);
        UUID consultationId = consultation.getId();

        when(consultationRepository.findById(consultationId)).thenReturn(Optional.of(consultation));
        doThrow(InvalidConsultationTimeException.class).when(consultationUpdateValidation).validation(consultation);

        assertThrows(
                InvalidConsultationTimeException.class,
                () -> consultationService.update(consultationId,updateRequest)
        );
        verify(consultationUpdateValidation).validation(consultation);
        verify(consultationRepository).findById(consultationId);
        verify(consultationRepository, never()).save(any());
    }

    @Test
    void test_update_consultation_doctor_unavailable(){
        LocalDateTime validDateTime =
                LocalDateTime.now().plusDays(1)
                        .withHour(10)
                        .withMinute(0)
                        .withSecond(0)
                        .withNano(0);
        Consultation consultation = ConsultationMother.consultatioWithTime(validDateTime);
        ConsultationUpdateRequest updateRequest = ConsultationMother.updateRequestWithTime(validDateTime);
        UUID consultationId = consultation.getId();

        when(consultationRepository.findById(consultation.getId())).thenReturn(Optional.of(consultation));
        doThrow(DoctorUnavailableBusinessException.class).when(consultationUpdateValidation).validation(consultation);

        assertThrows(
                DoctorUnavailableBusinessException.class,
                () -> consultationService.update(consultationId,updateRequest)
        );
        verify(consultationUpdateValidation).validation(consultation);
        verify(consultationRepository).findById(consultationId);
        verify(consultationRepository, never()).save(any());
    }

    @Test
    void test_update_consultation_invalid_doctor(){
        Consultation consultation = ConsultationMother.consultationBase(UUID.randomUUID());
        UUID consultationId = consultation.getId();
        ConsultationUpdateRequest updateRequest = ConsultationMother.updateRequest();

        when(consultationRepository.findById(consultationId)).thenReturn(Optional.of(consultation));
        doThrow(InvalidDoctorUserException.class).when(consultationUpdateValidation).validation(consultation);

        assertThrows(
                InvalidDoctorUserException.class,
                () -> consultationService.update(consultationId, updateRequest)
        );
        verify(consultationUpdateValidation).validation(consultation);
        verify(consultationRepository).findById(consultationId);
        verify(consultationRepository, never()).save(any());
    }

    @Test
    void test_confirm_consultation(){
        Consultation  consultation = ConsultationMother.consultationBase(UUID.randomUUID());
        UUID consultationId = consultation.getId();

        when(consultationRepository.findById(consultationId)).thenReturn(Optional.of(consultation));
        when(consultationRepository.save(any(Consultation.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Consultation result = consultationService.confirmConsultation(consultationId);

        assertThat(result, is(consultation));
        assertThat(consultation.getStatus(), is(ConsultationStatus.AGENDADA));
    }

    @Test
    void test_cancel_consultation(){
        Consultation  consultation = ConsultationMother.consultationBase(UUID.randomUUID());
        UUID consultationId = consultation.getId();

        when(consultationRepository.findById(consultationId)).thenReturn(Optional.of(consultation));
        when(consultationRepository.save(any(Consultation.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Consultation result = consultationService.cancelConsultation(consultationId);

        assertThat(result, is(consultation));
        assertThat(consultation.getStatus(), is(ConsultationStatus.CANCELADA));
    }

    @Test
    void test_finalize_consultation(){
        Consultation  consultation = ConsultationMother.consultationBase(UUID.randomUUID());
        UUID consultationId = consultation.getId();

        when(consultationRepository.findById(consultationId)).thenReturn(Optional.of(consultation));
        when(consultationRepository.save(any(Consultation.class)))
                        .thenAnswer(invocation -> invocation.getArgument(0));
        Consultation result = consultationService.finishConsultation(consultationId);

        assertThat(result, is(consultation));
        assertThat(consultation.getStatus(), is(ConsultationStatus.FINALIZADA));
    }

    @Test
    void test_confirm_consultation_not_found(){
        Consultation  consultation = ConsultationMother.consultationBase(UUID.randomUUID());
        UUID consultationId = consultation.getId();

        when(consultationRepository.findById(consultationId)).thenReturn(Optional.empty());

        assertThrows(
                ConsultationNotFoundException.class,
                () -> consultationService.confirmConsultation(consultationId)
        );
        verify(consultationRepository).findById(consultationId);
        verify(consultationRepository, never()).save(any());
    }

    @Test
    void test_cancel_consultation_not_found(){
        Consultation  consultation = ConsultationMother.consultationBase(UUID.randomUUID());
        UUID consultationId = consultation.getId();

        when(consultationRepository.findById(consultationId)).thenReturn(Optional.empty());

        assertThrows(
                ConsultationNotFoundException.class,
                () -> consultationService.cancelConsultation(consultationId)
        );
        verify(consultationRepository).findById(consultationId);
        verify(consultationRepository, never()).save(any());
    }

    @Test
    void test_finalize_consultation_not_found(){
        Consultation  consultation = ConsultationMother.consultationBase(UUID.randomUUID());
        UUID consultationId = consultation.getId();

        when(consultationRepository.findById(consultationId)).thenReturn(Optional.empty());

        assertThrows(
                ConsultationNotFoundException.class,
                () -> consultationService.finishConsultation(consultationId)
        );
        verify(consultationRepository).findById(consultationId);
        verify(consultationRepository, never()).save(any());
    }

}
