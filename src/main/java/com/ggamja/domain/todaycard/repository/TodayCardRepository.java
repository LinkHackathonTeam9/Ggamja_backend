package com.ggamja.domain.todaycard.repository;

import com.ggamja.domain.todaycard.entity.TodayCard;
import com.ggamja.domain.card.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface TodayCardRepository extends JpaRepository<TodayCard, Long> {
    Optional<TodayCard> findByDateAndCard_Category(LocalDate date, Category category);
}