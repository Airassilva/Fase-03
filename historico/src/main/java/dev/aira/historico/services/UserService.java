package dev.aira.historico.services;

import dev.aira.historico.dtos.UserDTO;
import dev.aira.historico.entities.User;
import dev.aira.historico.feign.UserClient;
import dev.aira.historico.mappers.UserMapper;
import dev.aira.historico.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserClient  userClient;

    @Transactional
    public User getOrCreate(UUID id) {
        return userRepository.findById(id).orElseGet(() -> {
                    UserDTO dto = userClient.buscarPorId(id);
                    return userRepository.save(UserMapper.toEntity(dto));
        });
    }
}
