package com.alberinando.imagelite.web.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class responseImageDTO {
    private String url;
    private String extension;
    private String name;
    private long size;
    private LocalDate uploadDate;
}
