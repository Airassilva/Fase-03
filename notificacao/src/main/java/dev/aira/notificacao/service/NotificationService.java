package dev.aira.notificacao.service;

import dev.aira.notificacao.ConsultationEvents.ConsultationCreated;
import dev.aira.notificacao.dtos.DoctorDTO;
import dev.aira.notificacao.dtos.ReminderEvent;
import dev.aira.notificacao.dtos.UserDTO;
import dev.aira.notificacao.enums.ConsultationStatus;
import dev.aira.notificacao.enums.ReminderType;
import dev.aira.notificacao.enums.UserType;
import dev.aira.notificacao.exceptions.ConsultationConsumerException;
import dev.aira.notificacao.exceptions.EmailUserNotFoundException;
import dev.aira.notificacao.exceptions.StatusUserNotFoundException;
import dev.aira.notificacao.feign.UserClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final UserClient userClient;
    private final EmailService emailService;

    public void notifyByEmail(ConsultationCreated event) {
        UUID userId = UUID.fromString(event.getUserId());
        UUID doctorID = UUID.fromString(event.getDoctorId());

        UserDTO patient = searchPatient(userId);
        DoctorDTO doctor = searchDoctor(doctorID);

        if (patient.getEmail() == null) {
            throw new EmailUserNotFoundException(patient.getId());
        }

        log.info("Paciente carregado {}", patient.getEmail());

        String subject = "Atualização da sua consulta";
        String body = assembleEmail(patient, doctor ,event);

        emailService.send(
                patient.getEmail(),
                subject,
                body
        );
    }

    public void notifyReminder(ReminderEvent event) {

        log.info("Enviando reminder {}", event);
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

        log.info("Enviando lembrete para confirmar consulta {}", patient.getEmail());
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
        log.info("enviando lembrete de consulta confirmada {}", patient.getEmail());
        emailService.send(patient.getEmail(), subject, body);
    }

    private UserDTO searchPatient(UUID userId) {

        try {
            log.info("Buscando paciente {}", userId);

            var user = userClient.buscarPorId(userId);

            log.info("Tipo do usuario {}", user.getType());

            if (user.getType() != UserType.PACIENTE) {
                throw new StatusUserNotFoundException(userId);
            }
            return new UserDTO(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getType()
            );
        } catch (FeignException.NotFound e) {
            log.warn("Paciente {} não encontrado no agendamento-service", userId);
            throw new EmailUserNotFoundException(userId);
        } catch (FeignException e) {
            log.error("Erro ao chamar agendamento-service para user {}", userId, e);
            throw new ConsultationConsumerException(
                    "Erro ao buscar paciente no agendamento-service", e
            );
        }
    }


    private DoctorDTO searchDoctor(UUID doctorId) {
        try {
            var user = userClient.buscarPorId(doctorId);

            if (user.getType() != UserType.MEDICO) {
                throw new StatusUserNotFoundException(doctorId);
            }
            log.info("Médico carregado {}", user.getName());
            return new DoctorDTO(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getType()
            );
        }catch (FeignException.NotFound e) {
            log.warn("Medico {} não encontrado no agendamento-service", doctorId);
            throw new EmailUserNotFoundException(doctorId);
        } catch (FeignException e) {
            log.error("Erro ao chamar agendamento-service para user {}", doctorId, e);
            throw new ConsultationConsumerException(
                    "Erro ao buscar medico no agendamento-service", e
            );
        }
    }

    private String assembleEmail(UserDTO user,DoctorDTO doctor, ConsultationCreated event) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime consultationDate = LocalDateTime.parse(event.getConsultationDate(), timeFormatter);
        ConsultationStatus status = ConsultationStatus.valueOf(event.getConsultationStatus());
        log.info("Enviando email...");
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
