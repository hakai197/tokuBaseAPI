package com.tokubase.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "series")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Series {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeriesType type;

    @Column(nullable = false)
    private Integer yearStart;

    private Integer yearEnd;

    @Column(length = 2000)
    private String description;

    @OneToMany(mappedBy = "series", cascade = CascadeType.ALL)
    private List<Character> characters;

    // Series images
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String logoBase64;           // Series logo

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String posterBase64;         // Series poster/cover art

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String bannerBase64;         // Banner image for headers

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String seriesImageBase64;    // ADD THIS - Main series image

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String thumbnailBase64;      // ADD THIS - Thumbnail image

    private String seriesImageUrl;       // For cloud storage URLs
}