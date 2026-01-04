package dev.aira.historico.mappers;

import dev.aira.historico.dtos.UserDTO;
import dev.aira.historico.entities.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    public static User toEntity(UserDTO dto){
        return new User(
                dto.getId(),
                dto.getName(),
                dto.getEmail(),
                dto.getUserType()
        );
    }

    public static UserDTO toDTO(User patient) {
        return new UserDTO(
                patient.getId(),
                patient.getName(),
                patient.getEmail(),
                patient.getUserType()
        );
    }
}
