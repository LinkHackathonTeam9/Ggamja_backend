package com.ggamja.domain.card.entity;

import com.ggamja.domain.quiz.entity.Quiz;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "card",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"category", "title"})
        }
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, length = 255)
    private String meaning;

    @Column(nullable = false)
    private int difficulty;   // 난이도 (1 ~ 3)

    @OneToOne(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Quiz quiz;
}