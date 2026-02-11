package com.tokubase.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "characters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String color;

    @ManyToOne
    @JoinColumn(name = "series_id", nullable = false)
    private Series series;

    @OneToMany(mappedBy = "character", cascade = CascadeType.ALL)
    private List<Form> forms;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CharacterRole role;

    // Character images
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String characterImageBase64;  // Main character image

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String portraitBase64;        // Portrait/headshot

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String thumbnailBase64;       // ADD THIS - Thumbnail image

    private String characterImageUrl;     // For cloud storage URLs
}