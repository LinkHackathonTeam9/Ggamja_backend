package com.ggamja.domain.quizlog.dto.response;

import com.ggamja.domain.quiz.entity.Quiz;
import com.ggamja.domain.quizlog.entity.QuizLog;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record GetQuizLogDetailResponse(
        @Schema(description = "퀴즈 로그 ID", example = "4")
        Long quizLogId,

        @Schema(description = "정답 여부", example = "true")
        boolean isCorrect,

        @Schema(description = "선택한 답안", example = "1번")
        String selectedAnswer,

        @Schema(description = "퀴즈 푼 날짜", example = "2025-08-28T04:41:06")
        LocalDateTime date,

        @Schema(description = "퀴즈 상세 정보")
        QuizDto quiz
) {
    public static GetQuizLogDetailResponse of(QuizLog quizLog) {
        return new GetQuizLogDetailResponse(
                quizLog.getId(),
                quizLog.isCorrect(),
                quizLog.getSelectedAnswer(),
                quizLog.getDate(),
                QuizDto.of(quizLog.getQuiz())
        );
    }

    public record QuizDto(
            @Schema(description = "퀴즈 ID", example = "304")
            Long quizId,

            @Schema(description = "퀴즈 문제", example = "세상에서 제일 큰 동물은?")
            String question,

            @Schema(description = "선지 1", example = "대왕오징어")
            String option1,

            @Schema(description = "선지 2", example = "아프리카코끼리")
            String option2,

            @Schema(description = "선지 3", example = "바다악어")
            String option3,

            @Schema(description = "정답 선지", example = "흰긴수염고래")
            String answer
    ) {
        public static QuizDto of(Quiz quiz) {
            return new QuizDto(
                    quiz.getId(),
                    quiz.getQuestion(),
                    quiz.getOption1(),
                    quiz.getOption2(),
                    quiz.getOption3(),
                    quiz.getAnswer()
            );
        }
    }
}
