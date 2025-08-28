package com.ggamja.domain.todayquiz.repository;

import com.ggamja.domain.member.entity.Member;
import com.ggamja.domain.todayquiz.entity.TodayQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface TodayQuizRepository extends JpaRepository<TodayQuiz, Long> {
    Optional<TodayQuiz> findByDate(LocalDate date);
}
