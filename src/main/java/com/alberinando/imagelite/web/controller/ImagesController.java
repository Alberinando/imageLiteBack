package com.alberinando.imagelite.web.controller;

import com.alberinando.imagelite.domain.entities.Image;
import com.alberinando.imagelite.domain.entities.enums.ImageExtension;
import com.alberinando.imagelite.domain.services.ImageServices;
import com.alberinando.imagelite.infrastructure.mapper.ImageMapper;
import com.alberinando.imagelite.web.dto.createImageDTO;
import com.alberinando.imagelite.web.dto.responseImageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/images")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin("*")
public class ImagesController {

    private final ImageServices imageServices;
    private final ImageMapper imageMapper;

    @PostMapping
    public ResponseEntity save(@ModelAttribute createImageDTO createImageDTO) throws IOException {

        Image image = imageMapper.mapToImage(createImageDTO);
        Image savedImage = imageServices.save(image);
        URI imageUri = getImageURL(savedImage);

        return ResponseEntity.created(imageUri).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable String id) {
        var possibleImag = imageServices.findById(id);
        if (possibleImag.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        var image = possibleImag.get();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(image.getExtension().getMediaType()));
        headers.setContentLength(image.getSize());
        headers.setContentDispositionFormData("inline; filename=\"" + image.getFileName() +  "\"", image.getFileName());
        return new ResponseEntity<>(image.getFile(), headers, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<responseImageDTO>> search(@RequestParam(value = "Extension", required = false, defaultValue = "") String extension, @RequestParam(value = "Query", required = false) String query) {
        var result = imageServices.search(ImageExtension.ofName(extension), query);
        var images = result.stream().map(image -> {
            var url = getImageURL(image);
            return imageMapper.imageToDTO(image, url.toString());
        }).collect(Collectors.toList());
        return ResponseEntity.ok(images);
    }

    private URI getImageURL(Image image) {
        String imagePath = "/" + image.getId();
        ServletUriComponentsBuilder.fromCurrentRequest();
        return ServletUriComponentsBuilder.fromCurrentRequestUri().path(imagePath).build().toUri();
    }
}
