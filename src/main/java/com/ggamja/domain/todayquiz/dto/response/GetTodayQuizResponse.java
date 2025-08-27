package com.ggamja.domain.todayquiz.dto.response;

import com.ggamja.domain.quiz.entity.Quiz;

import java.util.List;

public record GetTodayQuizResponse(
        Long id,
        String question,
        String answer,
        List<String> options
) {
    public static GetTodayQuizResponse from(Quiz quiz) {
        return new GetTodayQuizResponse(
                quiz.getId(),
                quiz.getQuestion(),
                quiz.getAnswer(),
                List.of(quiz.getOption1(),
                        quiz.getOption2(),
                        quiz.getOption3())
                );
    }
}
