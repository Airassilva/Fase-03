package dev.aira.notificacao.service;

import dev.aira.notificacao.ConsultationEvents.ConsultationCreated;
import dev.aira.notificacao.dtos.DoctorDTO;
import dev.aira.notificacao.dtos.ReminderEvent;
import dev.aira.notificacao.dtos.UserDTO;
import dev.aira.notificacao.enums.ConsultationStatus;
import dev.aira.notificacao.enums.ReminderType;
import dev.aira.notificacao.enums.UserType;
import dev.aira.notificacao.exceptions.EmailUserNotFoundException;
import dev.aira.notificacao.exceptions.StatusUserNotFoundException;
import dev.aira.notificacao.feign.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final UserClient userClient;
    private final EmailService emailService;

    public void notifyByEmail(ConsultationCreated event) {

        UUID userId = UUID.fromString(event.getUserId());
        UserDTO patient = searchPatient(userId);
        DoctorDTO doctor = searchDoctor(userId);

        if (patient.getEmail() == null) {
            throw new EmailUserNotFoundException(patient.getId());
        }

        String subject = "Atualização da sua consulta";
        String body = assembleEmail(patient, doctor, event);

        emailService.send(
                patient.getEmail(),
                subject,
                body
        );
    }

    public void notifyReminder(ReminderEvent event) {

        UserDTO patient = searchPatient(event.getUserId());

        if (patient.getEmail() == null) {
            throw new EmailUserNotFoundException(patient.getId());
        }

        if (event.getType() == ReminderType.CONFIRMATION) {
            sendConfirmationEmail(patient);
        }

        if (event.getType() == ReminderType.UPCOMING) {
            sendUpcomingReminderEmail(patient);
        }
    }

    private void sendConfirmationEmail(UserDTO patient) {

        String subject = "Confirme sua consulta";
        String body = """
        Olá, %s!

        Sua consulta ainda não foi confirmada.
        Por favor, confirme até 2 dias antes do horário agendado.

        Atenciosamente,
        Consultorio LTDA
        """.formatted(patient.getName());

        emailService.send(patient.getEmail(), subject, body);
    }

    private void sendUpcomingReminderEmail(UserDTO patient) {

        String subject = "Lembrete de consulta";
        String body = """
        Olá, %s!

        Este é um lembrete de que sua consulta acontecerá em breve.
        Faltam apenas 2 horas.

        Atenciosamente,
        Consultorio LTDA
        """.formatted(patient.getName());

        emailService.send(patient.getEmail(), subject, body);
    }

    private UserDTO searchPatient(UUID userId) {

        var user = userClient.buscarPorId(userId);

        if (user.getType() != UserType.PACIENTE) {
            throw new StatusUserNotFoundException(userId);
        }

        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getType()
        );
    }

    private DoctorDTO searchDoctor(UUID doctorId) {

        var user = userClient.buscarPorId(doctorId);

        if (user.getType() != UserType.MEDICO) {
            throw new StatusUserNotFoundException(doctorId);
        }

        return new DoctorDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getType()
        );
    }

    private String assembleEmail(UserDTO user, DoctorDTO doctor, ConsultationCreated event) {
        LocalDateTime consultationDate = LocalDateTime.parse(event.getConsultationDate());
        ConsultationStatus status = ConsultationStatus.valueOf(event.getConsultationStatus());
        return """
            Olá, %s!

            Sua consulta com o médico %s,
            agendada para %s,
            está com o status: %s.

            Atenciosamente,
            Consultorio LTDA
            """.formatted(
                user.getName(),
                doctor.getName(),
                consultationDate,
                status
        );
    }
}
