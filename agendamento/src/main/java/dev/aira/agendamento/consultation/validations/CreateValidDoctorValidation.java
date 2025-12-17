package dev.aira.agendamento.consultation.validations;

import dev.aira.agendamento.consultation.entities.Consultation;
import dev.aira.agendamento.exceptions.DoctorNotFoundException;
import dev.aira.agendamento.exceptions.InvalidDoctorUserException;
import dev.aira.agendamento.user.entities.User;
import dev.aira.agendamento.user.entities.UserType;
import dev.aira.agendamento.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateValidDoctorValidation implements ConsultationCreateValidation {
    private final UserRepository userRepository;

    @Override
    public void validation(Consultation consultation) {
        User doctor = userRepository.findById(consultation.getDoctorId())
                .orElseThrow(DoctorNotFoundException::new);

        if(doctor.getUserType() != UserType.MEDICO){
            throw new InvalidDoctorUserException();
        }
    }
}
