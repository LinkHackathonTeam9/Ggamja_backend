package com.ggamja.domain.todaycard.entity;

import com.ggamja.domain.card.entity.Card;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "today_card")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodayCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @Column(nullable = false)
    private LocalDate date;
}
