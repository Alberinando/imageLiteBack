package com.alberinando.imagelite.domain.services;

import com.alberinando.imagelite.domain.entities.User;
import com.alberinando.imagelite.domain.repositories.UserRepository;
import com.alberinando.imagelite.domain.services.servicesImpl.userServicesImpl;
import com.alberinando.imagelite.infrastructure.config.AccessToken;
import com.alberinando.imagelite.infrastructure.exceptions.DuplicatedTupleException;
import com.alberinando.imagelite.infrastructure.security.Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServices implements userServicesImpl {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Jwt token;

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        var possibleUser = findUserByEmail(user.getEmail());
        if (possibleUser != null) {
            throw new DuplicatedTupleException("Usuário já criado");
        }
        encodePassword(user);
        return userRepository.save(user);
    }

    @Override
    public AccessToken authenticate(String email, String password) {
        var user = findUserByEmail(email);
        if (user == null) {
            return null;
        }

        boolean matches = passwordEncoder.matches(password, user.getPassword());

        if (matches) {
            return token.getAccessToken(user);
        }

        return null;
    }

    private void encodePassword(User user) {
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
    }
}
