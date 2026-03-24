package com.tokubase.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "forms",
    indexes = {
        @Index(name = "idx_form_character",  columnList = "character_id"),
        @Index(name = "idx_form_final_form", columnList = "is_final_form")
    }
)
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

    @Column(name = "power_type", length = 100)
    private String powerType;

    @Column(name = "is_final_form")
    private Boolean isFinalForm = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", nullable = false)
    @ToString.Exclude
    private Character character;

    @Lob @Column(name = "form_image_base64", columnDefinition = "LONGTEXT")
    private String formImageBase64;
    @Lob @Column(name = "icon_base64", columnDefinition = "LONGTEXT")
    private String iconBase64;
    @Lob @Column(name = "thumbnail_base64", columnDefinition = "LONGTEXT")
    private String thumbnailBase64;
    @Column(name = "form_image_url", length = 500)
    private String formImageUrl;
}