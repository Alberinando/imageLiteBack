package com.alberinando.imagelite.web.controller;

import com.alberinando.imagelite.domain.entities.User;
import com.alberinando.imagelite.domain.services.UserServices;
import com.alberinando.imagelite.infrastructure.exceptions.DuplicatedTupleException;
import com.alberinando.imagelite.infrastructure.mapper.UserMapper;
import com.alberinando.imagelite.web.dto.users.CredentialsDTO;
import com.alberinando.imagelite.web.dto.users.createUsersDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/user")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {

    private final UserServices userServices;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity save(@RequestBody createUsersDTO CreateUsersDTO) {
        try {
            User user = userMapper.mapToUser(CreateUsersDTO);
            userServices.saveUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (DuplicatedTupleException e){
            Map<String, String> jsonResult = Map.of("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(jsonResult);
        }
    }

    @PostMapping("/auth")
    public ResponseEntity aurhentication(@RequestBody CredentialsDTO credentialsDTO) {
        var token = userServices.authenticate(credentialsDTO.getEmail(), credentialsDTO.getPassword());
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok().body(token);
    }
}
