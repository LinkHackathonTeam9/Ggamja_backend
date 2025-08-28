package com.ggamja.domain.todayquiz.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record GetTodayQuizStatusResponse(
        @Schema(description = "오늘의 퀴즈 풀이 여부", example = "true")
        boolean isSolved
) {
    public static GetTodayQuizStatusResponse of(boolean isSolved) {
        return new GetTodayQuizStatusResponse(isSolved);
    }
}
