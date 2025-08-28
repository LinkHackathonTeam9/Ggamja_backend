package com.ggamja.domain.quizlog.repository;

import com.ggamja.domain.member.entity.Member;
import com.ggamja.domain.quiz.entity.Quiz;
import com.ggamja.domain.quizlog.entity.QuizLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface QuizLogRepository extends JpaRepository<QuizLog, Long> {
    Page<QuizLog> findByMember(Member member, Pageable pageable);
    Optional<QuizLog> findByIdAndMember(Long id, Member member);

    @Query("""
    SELECT CASE WHEN COUNT(q) > 0 THEN true ELSE false END
    FROM QuizLog q
    WHERE q.member = :member
    AND DATE(q.date) = :date
    """)
    boolean existsByMemberAndDate(Member member, LocalDate date);
}
