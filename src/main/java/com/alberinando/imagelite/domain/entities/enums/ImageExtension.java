package com.alberinando.imagelite.domain.entities.enums;

import lombok.Getter;
import org.springframework.http.MediaType;
import java.util.Arrays;

public enum ImageExtension {
    PNG(MediaType.IMAGE_PNG_VALUE),
    JPEG("image/jpeg"),
    GIF("image/gif"),
    BMP("image/bmp"),
    TIFF("image/tiff"),
    TIFF_GIF("image/tiff"),
    WEBP("image/webp"),
    WEBP_SNAPSHOT("image/webp"),
    PNG_SNAPSHOT(MediaType.IMAGE_PNG_VALUE),
    JPEG_SNAPSHOT(MediaType.IMAGE_JPEG_VALUE);

    @Getter
    private final String mediaType;

    ImageExtension(String mediaType) {
        this.mediaType = mediaType;
    }

    public static ImageExtension valueOfExtension (MediaType mediaType) {
        if (mediaType == null) {
            return null;
        }
        return Arrays.stream(values())
                .filter(ie -> ie.mediaType.equals(mediaType.toString()))
                .findFirst()
                .orElse(null);
    }

    public static ImageExtension ofName(String name) {
        if (name == null) {
            return null;
        }
        return Arrays.stream(values())
                .filter(ie -> ie.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
