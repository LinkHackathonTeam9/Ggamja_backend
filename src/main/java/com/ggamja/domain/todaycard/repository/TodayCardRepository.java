package com.ggamja.domain.todaycard.repository;

import com.ggamja.domain.todaycard.entity.TodayCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TodayCardRepository extends JpaRepository<TodayCard, Long> {
    List<TodayCard> findByDate(LocalDate date);
}