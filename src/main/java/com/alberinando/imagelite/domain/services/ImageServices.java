package com.alberinando.imagelite.domain.services;

import com.alberinando.imagelite.domain.entities.Image;
import com.alberinando.imagelite.domain.repositories.ImageRepository;
import com.alberinando.imagelite.domain.services.servicesImpl.imageServicesImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageServices implements imageServicesImpl {

    private final ImageRepository imageRepository;

    @Override
    @Transactional
    public Image save(Image image) {
        return imageRepository.save(image);
    }

    @Override
    public Optional<Image> findById(String id) {
        return imageRepository.findById(id);
    }
}
