package dev.aira.agendamento.user.service;

import dev.aira.agendamento.exceptions.EmailNotFoundException;
import dev.aira.agendamento.exceptions.UserNotFoundException;
import dev.aira.agendamento.user.dtos.UserUpdateRequest;
import dev.aira.agendamento.user.entities.User;
import dev.aira.agendamento.user.repositories.UserRepository;
import dev.aira.agendamento.user.validations.UserCreateValidation;
import dev.aira.agendamento.user.validations.UserUpdateValidation;
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
    private final List<UserUpdateValidation> userUpdateValidation;

    public User create(User user) {
        userCreateValidation.forEach(v -> v.validation(user));
        return userRepository.save(user);
    }

    public User update (UUID id, UserUpdateRequest updateRequest) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        userUpdateValidation.forEach(v -> v.validation(user));
        user.update(updateRequest);
        return userRepository.save(user);
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

    public void desactiveUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        user.deactivate();
        userRepository.save(user);
    }

    public void activateUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        user.activate();
        userRepository.save(user);
    }

    public void delete(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
    }
}
