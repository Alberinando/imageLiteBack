package com.alberinando.imagelite.domain.repositories;

import com.alberinando.imagelite.domain.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, String> {
}
