package dev.aira.agendamento.user.service;

import dev.aira.agendamento.exceptions.EmailNotFoundException;
import dev.aira.agendamento.exceptions.UserNotFoundException;
import dev.aira.agendamento.user.entities.User;
import dev.aira.agendamento.user.repositories.UserRepository;
import dev.aira.agendamento.user.validations.UserCreateValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final List<UserCreateValidation> userCreateValidation;

    public User create(User user) {
        userCreateValidation.forEach(v -> v.valida(user));
        return userRepository.save(user);
    }

    public User update (User user) {
        return userRepository.save(user);
    }

    public User findByEmail (String email) {
        return userRepository.findByEmail(email).orElseThrow(EmailNotFoundException::new);
    }
    
    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void delete(UUID id) {
        userRepository.deleteById(id);
    }
}
