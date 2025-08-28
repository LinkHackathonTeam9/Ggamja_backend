package com.ggamja.domain.studylog.repository;

import com.ggamja.domain.card.entity.Category;
import com.ggamja.domain.studylog.entity.StudyLog;
import com.ggamja.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface StudyLogRepository extends JpaRepository<StudyLog, Long> {
    Page<StudyLog> findByMember(Member member, Pageable pageable);
    @Query("SELECT DISTINCT s.card.category " +
            "FROM StudyLog s " +
            "WHERE s.member = :member " +
            "AND DATE(s.date) = :today")
    List<Category> findTodayCategoriesByMember(@Param("member") Member member, @Param("today") LocalDate today);

    @Query("""
        SELECT COUNT(DISTINCT sl.card.id)
        FROM StudyLog sl
        JOIN sl.card c
        WHERE sl.member = :member
          AND c.category = :category
          AND sl.date BETWEEN :start AND :end
    """)
    Long countSolvedCardsByCategoryThisMonth(
            @Param("member") Member member,
            @Param("category") Category category,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}