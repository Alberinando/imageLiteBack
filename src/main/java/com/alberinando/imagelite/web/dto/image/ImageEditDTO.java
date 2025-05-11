package com.alberinando.imagelite.web.dto.image;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageEditDTO {
    private String id;
    private String name;
    private String fileBase64;
    private String tags;
}