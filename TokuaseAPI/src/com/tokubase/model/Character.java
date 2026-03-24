package com.tokubase.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "characters",
    indexes = {
        @Index(name = "idx_character_series", columnList = "series_id"),
        @Index(name = "idx_character_role",   columnList = "role")
    }
)
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

    @Column(length = 50)
    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id", nullable = false)
    @ToString.Exclude
    private Series series;

    @OneToMany(mappedBy = "character", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private List<Form> forms = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CharacterRole role;

    @Lob @Column(name = "character_image_base64", columnDefinition = "LONGTEXT")
    private String characterImageBase64;
    @Lob @Column(name = "portrait_base64", columnDefinition = "LONGTEXT")
    private String portraitBase64;
    @Lob @Column(name = "thumbnail_base64", columnDefinition = "LONGTEXT")
    private String thumbnailBase64;
    @Column(name = "character_image_url", length = 500)
    private String characterImageUrl;
}