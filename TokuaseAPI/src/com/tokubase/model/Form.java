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
}
