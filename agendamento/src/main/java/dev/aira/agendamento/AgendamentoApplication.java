package dev.aira.agendamento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class AgendamentoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgendamentoApplication.class, args);
    }
}
