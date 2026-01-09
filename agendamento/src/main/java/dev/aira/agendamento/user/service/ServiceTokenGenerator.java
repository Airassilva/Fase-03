package dev.aira.agendamento.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ServiceTokenGenerator implements CommandLineRunner {

    private final TokenService tokenService;

    @Override
    public void run(String... args) {
//        String token = tokenService.generateServiceToken("notificacao-service");
//        log.info("SERVICE TOKEN:");
//        log.info(token);
    }
}