package com.ggamja.domain.quizlog.repository;

import com.ggamja.domain.analysis.projection.CategoryStatProjection;
import com.ggamja.domain.member.entity.Member;
import com.ggamja.domain.quizlog.entity.QuizLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("""
    SELECT c.category AS category,
           COUNT(ql.id) AS quizLogCount,
           SUM(CASE WHEN ql.isCorrect = true THEN 1 ELSE 0 END) AS correctCount
    FROM Card c
    LEFT JOIN Quiz q ON q.card = c
    LEFT JOIN QuizLog ql ON ql.quiz = q AND ql.member = :member
                         AND ql.date BETWEEN :start AND :end
    GROUP BY c.category
""")
    List<CategoryStatProjection> findMonthlyCategoryStats(
            @Param("member") com.ggamja.domain.member.entity.Member member,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
