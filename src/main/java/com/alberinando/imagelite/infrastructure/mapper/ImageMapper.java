package com.alberinando.imagelite.infrastructure.mapper;

import com.alberinando.imagelite.domain.entities.Image;
import com.alberinando.imagelite.domain.entities.enums.ImageExtension;
import com.alberinando.imagelite.web.dto.image.ImageEditDTO;
import com.alberinando.imagelite.web.dto.image.createImageDTO;
import com.alberinando.imagelite.web.dto.image.responseImageDTO;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;

@Component
public class ImageMapper {

    public Image mapToImage(createImageDTO createImageDTO) throws IOException {
        long fileSize = createImageDTO.getFile().getSize();
        String fileExtesion = createImageDTO.getFile().getContentType();
        byte[] fileBytes = createImageDTO.getFile().getBytes();

        Image image = Image.builder()
                .name(createImageDTO.getName())
                .tags(String.join(",", createImageDTO.getTags()))
                .size(fileSize)
                .extension(ImageExtension.valueOfExtension(MediaType.valueOf(fileExtesion)))
                .file(fileBytes)
                .build();
        return image;
    }

    public responseImageDTO imageToDTO(Image image, String url){
        return responseImageDTO.builder()
                .id(image.getId())
                .url(url)
                .extension(image.getExtension().name())
                .name(image.getName())
                .size(image.getSize())
                .uploadDate(image.getUploadDate().toLocalDate())
                .build();
    }

    public ImageEditDTO imageToEditDTO(Image image) {
        String base64File = "";
        if (image.getFile() != null && image.getFile().length > 0) {
            base64File = Base64.getEncoder().encodeToString(image.getFile());
        }
        return ImageEditDTO.builder()
                .id(image.getId())
                .name(image.getName())
                .tags(image.getTags())
                .fileBase64(base64File)
                .build();
    }
}
