package com.alberinando.imagelite.domain.services.servicesImpl;

import com.alberinando.imagelite.domain.entities.Image;

import java.util.Optional;

public interface imageServicesImpl {
    Image save(Image image);

    Optional<Image> findById(String id);
}
