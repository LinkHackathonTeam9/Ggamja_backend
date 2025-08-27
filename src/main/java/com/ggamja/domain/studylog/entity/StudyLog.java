package com.ggamja.domain.studylog.entity;

import com.ggamja.domain.card.entity.Card;
import com.ggamja.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "study_log")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudyLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @Column(nullable = false, updatable = false) // 공부 일시 업데이트하지 않기로 했기 때문에 updatable=false
    private LocalDateTime date;

    @PrePersist // insert 시점에만 jpa가 이 값을 생성하도록 하는 어노테이션
    protected void onCreate() {
        this.date = LocalDateTime.now();
    }
}

