package com.ggamja.domain.quiz.repository;

import com.ggamja.domain.card.entity.Card;
import com.ggamja.domain.quiz.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByCard(Card card);
}