package com.ggamja.domain.level.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "level")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Level {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String level;

    @Column(name = "start_point", nullable = false, unique = true)
    private int startPoint;

    @Column(name = "character_url", nullable = false)
    private String characterUrl;
}

