package com.ggamja.domain.todayquiz.dto.response;

import com.ggamja.domain.quiz.entity.Quiz;
import com.ggamja.domain.todayquiz.entity.TodayQuiz;
import io.swagger.v3.oas.annotations.media.Schema;


public record PostTodayQuizSubmitResponse(
        @Schema(description = "오늘의 퀴즈 id", example = "12")
        Long todayQuizId,
        @Schema(description = "맞춤 여부", example = "true")
        boolean isCorrect,
        @Schema(description = "정답", example = "서울")
        String correctAnswer
) {
        public static PostTodayQuizSubmitResponse from(TodayQuiz todayQuiz, boolean isCorrect) {
                return new PostTodayQuizSubmitResponse(
                        todayQuiz.getId(),
                        isCorrect,
                        todayQuiz.getQuiz().getAnswer()
                );
        }
}
