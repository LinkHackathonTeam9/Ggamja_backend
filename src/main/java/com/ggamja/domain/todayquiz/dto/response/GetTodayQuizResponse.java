package com.ggamja.domain.todayquiz.dto.response;

import com.ggamja.domain.quiz.entity.Quiz;
import com.ggamja.domain.todayquiz.entity.TodayQuiz;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record GetTodayQuizResponse(
        @Schema(description = "오늘의 퀴즈 id", example = "12")
        Long todayQuizId,
        @Schema(description = "정답 카드 id", example = "1")
        Long cardId,
        @Schema(description = "퀴즈 문제", example = "한국의 수도는?")
        String question,
        @Schema(description = "퀴즈 답", example = "서울")
        String answer,
        @Schema(description = "퀴즈 선지 (정답 포함)", example = "[\"제주\", \"부산\", \"전주\", \"서울\"]")
        List<String> options
) {
    public static GetTodayQuizResponse from(TodayQuiz todayQuiz) {
        Quiz quiz = todayQuiz.getQuiz();

        List<String> options = new ArrayList<>();
        options.add(quiz.getAnswer());
        options.add(quiz.getOption1());
        options.add(quiz.getOption2());
        options.add(quiz.getOption3());

        Collections.shuffle(options);

        return new GetTodayQuizResponse(
                quiz.getId(),
                quiz.getCard().getId(),
                quiz.getQuestion(),
                quiz.getAnswer(),
                options
        );
    }
}
