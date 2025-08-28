package com.ggamja.domain.todaycard.repository;

import com.ggamja.domain.todaycard.entity.TodayCard;
import com.ggamja.domain.card.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

public interface TodayCardRepository extends JpaRepository<TodayCard, Long> {
    Optional<TodayCard> findByDateAndCard_Category(LocalDate date, Category category);
    List<TodayCard> findByDate(LocalDate date);
    // 이번 달, 특정 카테고리의 모든 오늘의카드 조회
    @Query("SELECT tc FROM TodayCard tc " +
            "WHERE tc.date BETWEEN :startDate AND :endDate " +
            "AND tc.card.category = :category")
    List<TodayCard> findByMonthAndCategory(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("category") Category category
    );
}
