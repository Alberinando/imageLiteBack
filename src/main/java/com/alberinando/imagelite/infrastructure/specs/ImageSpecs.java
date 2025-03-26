package com.alberinando.imagelite.infrastructure.specs;

import com.alberinando.imagelite.domain.entities.Image;
import com.alberinando.imagelite.domain.entities.enums.ImageExtension;
import org.springframework.data.jpa.domain.Specification;

public class ImageSpecs {

    private ImageSpecs() {}

    public static Specification<Image> extensionEquals(ImageExtension extension) {
        return (root, q, criteriaBuilder) -> criteriaBuilder.equal(root.get("extension"), extension);
    }

    public static Specification<Image> nameLike(String name) {
        return (root, q, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("name")), "%" + name.toUpperCase() + "%");
    }
    public static Specification<Image> tagsLike(String tags) {
        return (root, q, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("tags")), "%" + tags.toUpperCase() + "%");
    }
}
