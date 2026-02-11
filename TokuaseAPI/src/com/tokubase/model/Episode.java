package com.tokubase.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "episodes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Episode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer episodeNumber;

    @Column(nullable = false)
    private String title;

    private LocalDate airDate;

    @ManyToOne
    @JoinColumn(name = "series_id", nullable = false)
    private Series series;

    // Image fields
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String imageBase64;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String thumbnailBase64;

    private String imageUrl;
}