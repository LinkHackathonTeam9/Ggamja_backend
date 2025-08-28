package com.ggamja.domain.studylog.dto;

import com.ggamja.domain.card.entity.Category;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record GetStudyLogResponse(
        Long logId,
        Category category,
        String title,
        int difficulty,
        LocalDateTime date
) {}