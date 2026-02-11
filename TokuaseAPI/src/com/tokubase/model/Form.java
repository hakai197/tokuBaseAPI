package com.tokubase.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "forms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Form {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String powerType;

    private Boolean isFinalForm = false;

    @ManyToOne
    @JoinColumn(name = "character_id", nullable = false)
    private Character character;

    // Form images
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String formImageBase64;  // Main form image

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String iconBase64;       // Small icon/thumbnail

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String thumbnailBase64;  // ADD THIS - Thumbnail image

    private String formImageUrl;     // For cloud storage URLs
}