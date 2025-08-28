package com.ggamja.domain.card.repository;

import com.ggamja.domain.card.entity.Card;
import com.ggamja.domain.card.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByCategory(Category category);
}