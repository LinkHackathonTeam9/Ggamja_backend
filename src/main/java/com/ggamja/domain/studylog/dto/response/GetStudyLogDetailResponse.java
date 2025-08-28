package com.ggamja.domain.studylog.dto.response;

import com.ggamja.domain.card.entity.Category;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record GetStudyLogDetailResponse(
        Long logId,
        Long cardId,
        Category category,
        String title,
        String meaning,
        int difficulty,
        LocalDateTime date
) {}