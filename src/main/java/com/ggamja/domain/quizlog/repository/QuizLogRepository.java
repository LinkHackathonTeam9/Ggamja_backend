package com.ggamja.domain.quizlog.repository;

import com.ggamja.domain.member.entity.Member;
import com.ggamja.domain.quiz.entity.Quiz;
import com.ggamja.domain.quizlog.entity.QuizLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizLogRepository extends JpaRepository<QuizLog, Long> {
    Page<QuizLog> findByMember(Member member, Pageable pageable);

}
