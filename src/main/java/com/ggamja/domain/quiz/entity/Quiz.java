package com.ggamja.domain.quiz.entity;

import com.ggamja.domain.card.entity.Card;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "quiz")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String question;

    @Column(nullable = false, length = 255)
    private String option1;

    @Column(nullable = false, length = 255)
    private String option2;

    @Column(nullable = false, length = 255)
    private String option3;

    @Column(nullable = false, length = 255)
    private String answer;

    @Column(nullable = false)
    private int point;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false, unique = true)
    private Card card;
}