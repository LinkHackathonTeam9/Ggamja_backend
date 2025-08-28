package com.ggamja.domain.quizlog.dto.response;

import com.ggamja.domain.card.entity.Category;
import com.ggamja.domain.quizlog.entity.QuizLog;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public record GetQuizLogListResponse(
        @Schema(description = "퀴즈 로그 목록")
        List<QuizLogDto> quizLogs,

        @Schema(description = "다음 페이지 존재 여부", example = "true")
        boolean hasNext
) {
    public static GetQuizLogListResponse of(List<QuizLogDto> quizLogs, boolean hasNext) {
        return new GetQuizLogListResponse(quizLogs, hasNext);
    }

    public record QuizLogDto(
            @Schema(description = "퀴즈 로그 ID", example = "1")
            Long quizLogId,

            @Schema(description = "퀴즈 푼 날짜", example = "2025-08-28T10:12:30")
            LocalDateTime date,

            @Schema(description = "카테고리", example = "CAPITAL")
            Category category,

            @Schema(description = "정답 여부", example = "true")
            boolean isCorrect


    ) {
        public static QuizLogDto from(QuizLog quizLog) {
            return new QuizLogDto(
                    quizLog.getId(),
                    quizLog.getDate(),
                    quizLog.getQuiz().getCard().getCategory(),
                    quizLog.isCorrect()
            );
        }
    }
}