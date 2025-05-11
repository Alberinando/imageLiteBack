package com.alberinando.imagelite.web.controller;

import com.alberinando.imagelite.domain.entities.Image;
import com.alberinando.imagelite.domain.entities.enums.ImageExtension;
import com.alberinando.imagelite.domain.services.ImageServices;
import com.alberinando.imagelite.infrastructure.mapper.ImageMapper;
import com.alberinando.imagelite.web.dto.image.ImageEditDTO;
import com.alberinando.imagelite.web.dto.image.createImageDTO;
import com.alberinando.imagelite.web.dto.image.responseImageDTO;
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
import java.util.Optional;
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

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        Image deletedImage = imageServices.delete(id);
        if (deletedImage == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/edit/{id}")
    public ResponseEntity<ImageEditDTO> getImageForEdit(@PathVariable String id) {
        return imageServices.findById(id)
                .map(image -> {
                    ImageEditDTO editDTO = imageMapper.imageToEditDTO(image);
                    return ResponseEntity.ok(editDTO);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ImageEditDTO> updateImage(
            @PathVariable String id,
            @ModelAttribute createImageDTO updateDTO) throws IOException {

        Optional<Image> optImage = imageServices.findById(id);
        if(optImage.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Image image = optImage.get();
        image.setName(updateDTO.getName());
        image.setTags(String.join(",", updateDTO.getTags()));

        if (updateDTO.getFile() != null && !updateDTO.getFile().isEmpty()) {
            image.setFile(updateDTO.getFile().getBytes());
            image.setSize(updateDTO.getFile().getSize());
            String contentType = updateDTO.getFile().getContentType();
            image.setExtension(ImageExtension.valueOfExtension(MediaType.valueOf(contentType)));
        }
        Image updatedImage = imageServices.save(image);
        ImageEditDTO editDTO = imageMapper.imageToEditDTO(updatedImage);
        return ResponseEntity.ok(editDTO);
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
