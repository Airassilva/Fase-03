package dev.aira.agendamento.user.mapper;

import dev.aira.agendamento.user.dtos.UserRequest;
import dev.aira.agendamento.user.dtos.UserResponse;
import dev.aira.agendamento.user.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserRequest userRequest) {
        return new User(userRequest.getEmail(),
                        userRequest.getPassword(),
                        userRequest.getName(),
                        userRequest.getUserType());
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(user.getId(),
                                user.getName(),
                                user.getEmail(),
                                user.getUserType());
    }

    public Page<UserResponse> pageEntityToPageResponse(Page<User> users) {
        return users.map(this::toResponse);
    }

}
