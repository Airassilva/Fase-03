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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final UserService userService;

    public List<ConsultationDTO> myConsultations(ConsultationStatus status, String from, String to, Boolean onlyFuture) {
        boolean onlyFutureSafe = Boolean.TRUE.equals(onlyFuture);
        Jwt jwt = ((JwtAuthenticationToken)
                Objects.requireNonNull(SecurityContextHolder.getContext()
                        .getAuthentication()))
                .getToken();
        assert jwt != null;
        UUID patientId = UUID.fromString(jwt.getSubject());
        LocalDateTime fromDate = from != null ? LocalDateTime.parse(from) : null;
        LocalDateTime toDate = to != null ? LocalDateTime.parse(to) : null;
        List<Consultation> consultations =
                consultationRepository.findPatientConsultations(patientId,status, fromDate, toDate, onlyFutureSafe);
        return consultations.stream()
                .map(this::toDTO)
                .toList();
    }

    public List<ConsultationDTO> patientConsultations(UUID patientId, ConsultationStatus status, String from, String to, Boolean onlyFuture) {
        boolean onlyFutureSafe = Boolean.TRUE.equals(onlyFuture);
        LocalDateTime fromDate = from != null ? LocalDateTime.parse(from) : null;
        LocalDateTime toDate = to != null ? LocalDateTime.parse(to) : null;
        List<Consultation> consultations =
                consultationRepository.findPatientConsultations(patientId, status, fromDate, toDate, onlyFutureSafe);
        return consultations.stream()
                .map(this::toDTO)
                .toList();
    }

    public List<ConsultationDTO> allConsultations(ConsultationStatus status, String from, String to, Boolean onlyFuture) {
        LocalDateTime fromDate = from != null ? LocalDateTime.parse(from) : null;
        LocalDateTime toDate = to != null ? LocalDateTime.parse(to) : null;
        List<Consultation> consultations =
                consultationRepository.findAll( status, fromDate, toDate, onlyFuture);
        return consultations.stream()
                .map(this::toDTO)
                .toList();
    }

    private ConsultationDTO toDTO(Consultation consultation) {

        User patient = userService.getOrCreate(consultation.getPatientId());
        User doctor = userService.getOrCreate(consultation.getDoctorId());

        return new ConsultationDTO(
                consultation.getId(),
                consultation.getConsultationDate().toString(),
                consultation.getStatus(),
                UserMapper.toDTO(patient),
                UserMapper.toDTO(doctor));
    }

    @Transactional
    public ConsultationDTO update(UpdateConsultationInputDTO input) {
        Consultation consultation = consultationRepository.findById(input.getConsultationId())
                .orElseThrow(() -> new ConsultationNotFoundException(input.getConsultationId()));
        if (input.getDate() != null) {
            boolean updated = consultation.updateDate(LocalDateTime.parse(input.getDate()));

            if (!updated) {
                throw new InvalidConsultationDateException(LocalDateTime.parse(input.getDate()));
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
