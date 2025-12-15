package dev.aira.agendamento.user.service;

import dev.aira.agendamento.exceptions.EmailNotFoundException;
import dev.aira.agendamento.exceptions.UserNotFoundException;
import dev.aira.agendamento.user.dtos.UserUpdateRequest;
import dev.aira.agendamento.user.entities.User;
import dev.aira.agendamento.user.repositories.UserRepository;
import dev.aira.agendamento.user.validations.UserCreateValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public User update (UUID id, UserUpdateRequest updateRequest) {
        User usuarioEncontrado = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        usuarioEncontrado.update(updateRequest);
        return userRepository.save(usuarioEncontrado);
    }

    public User findByEmail (String email) {
        return userRepository.findByEmail(email).orElseThrow(EmailNotFoundException::new);
    }
    
    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public void delete(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
    }
}
