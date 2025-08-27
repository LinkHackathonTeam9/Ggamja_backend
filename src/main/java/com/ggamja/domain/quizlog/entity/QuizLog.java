package com.ggamja.domain.quizlog.entity;

import com.ggamja.domain.member.entity.Member;
import com.ggamja.domain.quiz.entity.Quiz;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "quiz_log")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Column(nullable = false, length = 255)
    private String selectedAnswer;   // 사용자가 선택한 정답

    @Column(nullable = false, updatable = false)
    private LocalDateTime date;

    @Column(nullable = false, name = "is_Correct")
    private boolean isCorrect;

    @PrePersist
    protected void onCreate() {
        this.date = LocalDateTime.now();
    }
}
