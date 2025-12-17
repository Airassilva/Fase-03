package dev.aira.agendamento.consultation.validations;

import dev.aira.agendamento.consultation.entities.Consultation;
import dev.aira.agendamento.exceptions.InvalidPatientUserException;
import dev.aira.agendamento.exceptions.PatientNotFoundException;
import dev.aira.agendamento.user.entities.User;
import dev.aira.agendamento.user.entities.UserType;
import dev.aira.agendamento.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateValidPatientValidation implements ConsultationCreateValidation{
    private final UserRepository userRepository;

    @Override
    public void validation(Consultation consultation) {
        User doctor = userRepository.findById(consultation.getPatientId())
                .orElseThrow(PatientNotFoundException::new);

        if(doctor.getUserType() != UserType.PACIENTE){
            throw new InvalidPatientUserException();
        }
    }
}
