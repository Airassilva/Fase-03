package dev.aira.historico.dtos;

import dev.aira.historico.enums.ConsultationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.graphql.data.method.annotation.SchemaMapping;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SchemaMapping(typeName = "Consultation")
public class ConsultationDTO {
    private UUID id;
    private String date;
    private ConsultationStatus status;
    private UserDTO patient;
    private UserDTO doctor;
}
