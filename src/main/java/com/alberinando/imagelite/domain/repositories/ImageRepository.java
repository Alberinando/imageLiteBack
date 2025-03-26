package com.alberinando.imagelite.domain.repositories;

import com.alberinando.imagelite.domain.entities.Image;
import com.alberinando.imagelite.domain.entities.enums.ImageExtension;
import com.alberinando.imagelite.infrastructure.specs.GenericSpecs;
import com.alberinando.imagelite.infrastructure.specs.ImageSpecs;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.util.StringUtils;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, String>, JpaSpecificationExecutor<Image> {

    default List<Image> findByExtensionAndNameOrTagsLike(ImageExtension extension, String query){
        //SELECT * FROM image WHERE 1 = 1
        Specification<Image> specification = Specification.where(GenericSpecs.conjunction());

        if(extension !=null){
            //AND EXTENSION = 'PNG'
            specification = specification.and(ImageSpecs.extensionEquals(extension));
        }

        if (StringUtils.hasText(query)){
            //AND (NAME LIKE 'QUERY' OR TAGS LIKE 'QUERY')
            specification = specification.and(Specification.anyOf(ImageSpecs.nameLike(query), ImageSpecs.tagsLike(query)));
        }
        return findAll(specification);
    }
}
