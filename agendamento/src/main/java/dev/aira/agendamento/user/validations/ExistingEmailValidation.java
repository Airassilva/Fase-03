package dev.aira.agendamento.user.validations;

import dev.aira.agendamento.exceptions.ExistingEmailException;
import dev.aira.agendamento.user.entities.User;
import dev.aira.agendamento.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExistingEmailValidation implements UserCreateValidation {

    private final UserRepository userRepository;

    @Override
    public void valida(User user) {
        User userEncontrado = userRepository.findByEmail(user.getEmail()).orElse(null);
        if (userEncontrado != null) {
            throw new ExistingEmailException();
        }
    }
}
