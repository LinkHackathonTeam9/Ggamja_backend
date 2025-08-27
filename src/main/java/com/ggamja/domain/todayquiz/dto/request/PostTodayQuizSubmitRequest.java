package com.ggamja.domain.todayquiz.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record PostTodayQuizSubmitRequest(
        @Schema(description = "제출한 답", example = "서울")
        @NotBlank
        String answer
) {
}
