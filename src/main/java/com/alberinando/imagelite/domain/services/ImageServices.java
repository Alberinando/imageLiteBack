package com.alberinando.imagelite.domain.services;

import com.alberinando.imagelite.domain.entities.Image;
import com.alberinando.imagelite.domain.entities.enums.ImageExtension;
import com.alberinando.imagelite.domain.repositories.ImageRepository;
import com.alberinando.imagelite.domain.services.servicesImpl.imageServicesImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    @Override
    public List<Image> search(ImageExtension imageExtension, String query) {
        return imageRepository.findByExtensionAndNameOrTagsLike(imageExtension, query);
    }

    @Override
    @Transactional
    public Image delete(String id) {
        return imageRepository.findById(id)
                .map(image -> {
                    imageRepository.delete(image);
                    return image;
                })
                .orElseThrow(() -> new RuntimeException("Imagem não encontrada!!!"));
    }

}
