package com.alberinando.imagelite.web.dto.image;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class createImageDTO {
    private MultipartFile file;
    private String name;
    private List<String> tags;
}
