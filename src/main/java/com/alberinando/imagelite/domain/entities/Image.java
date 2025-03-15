package com.alberinando.imagelite.domain.entities;

import com.alberinando.imagelite.domain.entities.enums.ImageExtension;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "Id")
    private String id;

    @Column(name = "Name")
    private String name;

    @Column(name = "Size")
    private Long size;

    @Column(name = "Extension")
    @Enumerated(EnumType.STRING)
    private ImageExtension extension;

    @Column(name = "UploadDate")
    @CreatedDate
    private LocalDateTime uploadDate;

    @Column(name = "Tags")
    private String tags;

    @Column(name = "File")
    @Lob
    private byte[] file;

    public String getFileName() {

        return getName().concat(".").concat(getExtension().name());
    }
}
