package com.tokubase.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(
    name = "episodes",
    uniqueConstraints = @UniqueConstraint(name = "uq_series_episode", columnNames = {"series_id", "episode_number"}),
    indexes = {
        @Index(name = "idx_episode_series",   columnList = "series_id"),
        @Index(name = "idx_episode_air_date", columnList = "air_date")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Episode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "episode_number", nullable = false)
    private Integer episodeNumber;

    @Column(nullable = false)
    private String title;

    @Column(name = "air_date")
    private LocalDate airDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id", nullable = false)
    @ToString.Exclude
    private Series series;

    @Lob @Column(name = "episode_image_base64", columnDefinition = "LONGTEXT")
    private String imageBase64;
    @Lob @Column(name = "thumbnail_base64", columnDefinition = "LONGTEXT")
    private String thumbnailBase64;
    @Column(name = "episode_image_url", length = 500)
    private String imageUrl;
}