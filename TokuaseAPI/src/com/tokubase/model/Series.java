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
}
