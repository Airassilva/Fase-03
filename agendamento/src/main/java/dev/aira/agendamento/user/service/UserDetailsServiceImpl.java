package dev.aira.agendamento.user.service;

import dev.aira.agendamento.exceptions.UserNotFoundException;
import dev.aira.agendamento.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @NotNull
    @Override
    public UserDetails loadUserByUsername(@NotNull String email)
            throws UserNotFoundException {

        return userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
    }
}

