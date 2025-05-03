package com.alberinando.imagelite.domain.repositories;

import com.alberinando.imagelite.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);
}
