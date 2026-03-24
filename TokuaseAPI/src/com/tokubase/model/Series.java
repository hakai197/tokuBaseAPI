package com.tokubase.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "series",
    uniqueConstraints = @UniqueConstraint(name = "uq_series_name", columnNames = "name"),
    indexes = {
        @Index(name = "idx_series_type", columnList = "type"),
        @Index(name = "idx_series_year", columnList = "year_start")
    }
)
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

    @Column(name = "year_start", nullable = false)
    private Integer yearStart;

    @Column(name = "year_end")
    private Integer yearEnd;

    @Column(length = 2000)
    private String description;

    @OneToMany(mappedBy = "series", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private List<Character> characters = new ArrayList<>();

    @OneToMany(mappedBy = "series", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private List<Episode> episodes = new ArrayList<>();

    @Lob @Column(name = "logo_base64", columnDefinition = "LONGTEXT")
    private String logoBase64;
    @Lob @Column(name = "poster_base64", columnDefinition = "LONGTEXT")
    private String posterBase64;
    @Lob @Column(name = "banner_base64", columnDefinition = "LONGTEXT")
    private String bannerBase64;
    @Lob @Column(name = "series_image_base64", columnDefinition = "LONGTEXT")
    private String seriesImageBase64;
    @Lob @Column(name = "thumbnail_base64", columnDefinition = "LONGTEXT")
    private String thumbnailBase64;
    @Column(name = "series_image_url", length = 500)
    private String seriesImageUrl;
}