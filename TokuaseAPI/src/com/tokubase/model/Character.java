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

    private String role; // MAIN, SECONDARY

    private String color;

    @ManyToOne
    @JoinColumn(name = "series_id", nullable = false)
    private Series series;

    @OneToMany(mappedBy = "character", cascade = CascadeType.ALL)
    private List<Form> forms;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CharacterRole role;
}
