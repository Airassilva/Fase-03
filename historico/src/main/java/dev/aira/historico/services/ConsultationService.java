package dev.aira.historico.services;

import dev.aira.historico.dtos.ConsultationDTO;
import dev.aira.historico.dtos.UpdateConsultationInputDTO;
import dev.aira.historico.entities.Consultation;
import dev.aira.historico.entities.User;
import dev.aira.historico.enums.ConsultationStatus;
import dev.aira.historico.enums.UserType;
import dev.aira.historico.exceptions.ConsultationNotFoundException;
import dev.aira.historico.exceptions.InvalidConsultationDateException;
import dev.aira.historico.exceptions.InvalidDoctorUserException;
import dev.aira.historico.mappers.UserMapper;
import dev.aira.historico.repositories.ConsultationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final UserService userService;

    public List<ConsultationDTO> myConsultations(ConsultationStatus status, LocalDateTime from, LocalDateTime to, Boolean onlyFuture) {
        UUID patientId = UUID.randomUUID(); //será mudado após add segurança
        //UUID patientId = SecurityUtils.getUserId(); //ajustar ao add segurança

        List<Consultation> consultations =
                consultationRepository.findPatientConsultations(patientId,status, from, to, onlyFuture);
        return consultations.stream()
                .map(this::toDTO)
                .toList();
    }

    public List<ConsultationDTO> patientConsultations(UUID patientId, ConsultationStatus status, LocalDateTime from, LocalDateTime to, Boolean onlyFuture) {
        List<Consultation> consultations =
                consultationRepository.findPatientConsultations(patientId, status, from, to, onlyFuture);
        return consultations.stream()
                .map(this::toDTO)
                .toList();
    }

    public List<ConsultationDTO> allConsultations(ConsultationStatus status, LocalDateTime from, LocalDateTime to, Boolean onlyFuture) {
        List<Consultation> consultations =
                consultationRepository.findAll(status, from, to, onlyFuture);
        return consultations.stream()
                .map(this::toDTO)
                .toList();
    }

    private ConsultationDTO toDTO(Consultation consultation) {

        User patient = userService.getOrCreate(consultation.getPatientId());
        User doctor = userService.getOrCreate(consultation.getDoctorId());

        return new ConsultationDTO(
                consultation.getId(),
                consultation.getConsultationDate(),
                consultation.getStatus(),
                UserMapper.toDTO(patient),
                UserMapper.toDTO(doctor));
    }

    @Transactional
    public ConsultationDTO update(UpdateConsultationInputDTO input) {
        Consultation consultation = consultationRepository.findById(input.getConsultationId())
                .orElseThrow(() -> new ConsultationNotFoundException(input.getConsultationId()));
        if (input.getDate() != null) {
            boolean updated = consultation.updateDate(input.getDate());

            if (!updated) {
                throw new InvalidConsultationDateException(input.getDate());
            }
        }
        if (input.getDoctorId() != null) {
            User doctor = userService.getOrCreate(input.getDoctorId());

            if (doctor.getUserType() != UserType.MEDICO) {
                throw new InvalidDoctorUserException(input.getDoctorId());
            }

            consultation.updateDoctor(doctor);
        }
        Consultation consultationUpdated = consultationRepository.save(consultation);
        return toDTO(consultationUpdated);
    }
}
