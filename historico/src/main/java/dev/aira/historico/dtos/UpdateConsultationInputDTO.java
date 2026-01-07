package dev.aira.historico.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.graphql.data.method.annotation.SchemaMapping;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SchemaMapping("UpdateConsultationInput")
public class UpdateConsultationInputDTO {
    private UUID consultationId;
    private LocalDateTime date;
    private UUID doctorId;
}
