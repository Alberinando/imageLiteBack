package com.alberinando.imagelite.domain.services.servicesImpl;

import com.alberinando.imagelite.domain.entities.User;
import com.alberinando.imagelite.infrastructure.config.AccessToken;

public interface userServicesImpl {
    User findUserByEmail(String email);
    User saveUser(User user);

    AccessToken authenticate(String email, String password);
}
