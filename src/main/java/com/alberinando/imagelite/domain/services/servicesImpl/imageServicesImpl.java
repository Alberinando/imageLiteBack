package com.alberinando.imagelite.domain.services.servicesImpl;

import com.alberinando.imagelite.domain.entities.Image;
import com.alberinando.imagelite.domain.entities.enums.ImageExtension;

import java.util.List;
import java.util.Optional;

public interface imageServicesImpl {
    Image save(Image image);

    Optional<Image> findById(String id);

    List<Image> search(ImageExtension imageExtension, String query);

    Image delete(String id);
}
