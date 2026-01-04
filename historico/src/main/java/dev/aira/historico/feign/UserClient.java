package dev.aira.historico.feign;

import dev.aira.historico.dtos.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "agendamento-service", url = "${services.agendamento.url}")
@Component
public interface UserClient {

    @GetMapping("/users/{id}")
    UserDTO buscarPorId(@PathVariable UUID id);
}

