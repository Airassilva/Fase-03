package dev.aira.historico.dtos;

import dev.aira.historico.enums.UserType;
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
@SchemaMapping(typeName = "User")
public class UserDTO {
    private UUID id;
    private String name;
    private String email;
    private UserType  userType;
}
