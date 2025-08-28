package com.ggamja.domain.todaycard.dto.response;

import com.ggamja.domain.card.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record GetTodayCardCategoriesResponse(
        @Schema(description = "오늘 학습한 카테고리 목록", example = "[\"CAPITAL\", \"PROVERB\"]")
        List<Category> categories
) {
    public static GetTodayCardCategoriesResponse of(List<Category> categories) {
        return new GetTodayCardCategoriesResponse(categories);
    }
}