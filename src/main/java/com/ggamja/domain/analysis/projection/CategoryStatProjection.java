package com.ggamja.domain.analysis.projection;


import com.ggamja.domain.card.entity.Category;

public interface CategoryStatProjection {
    Category getCategory();
    Long getQuizLogCount();
    Long getCorrectCount();
}