package com.alberinando.imagelite.infrastructure.mapper;

import com.alberinando.imagelite.domain.entities.User;
import com.alberinando.imagelite.web.dto.users.createUsersDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User mapToUser(createUsersDTO CreateUsersDTO) {
        return User
                .builder()
                .email(CreateUsersDTO.getEmail())
                .name(CreateUsersDTO.getName())
                .password(CreateUsersDTO.getPassword())
                .build();
    }
}
