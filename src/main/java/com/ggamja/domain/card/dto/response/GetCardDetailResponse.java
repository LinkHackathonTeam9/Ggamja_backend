package com.ggamja.domain.card.dto.response;

import com.ggamja.domain.card.entity.Category;
import lombok.Builder;

@Builder
public record GetCardDetailResponse(
        Long id,
        Category category,
        String title,
        String meaning,
        int difficulty
) {}
