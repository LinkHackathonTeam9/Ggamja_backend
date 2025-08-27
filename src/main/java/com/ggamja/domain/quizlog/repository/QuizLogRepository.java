package com.ggamja.domain.quizlog.repository;

import com.ggamja.domain.quiz.entity.Quiz;
import com.ggamja.domain.quizlog.entity.QuizLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizLogRepository extends JpaRepository<QuizLog, Long> {
}
