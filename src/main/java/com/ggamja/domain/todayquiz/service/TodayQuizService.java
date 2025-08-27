package com.ggamja.domain.todayquiz.service;

import com.ggamja.domain.quiz.entity.Quiz;
import com.ggamja.domain.todayquiz.dto.response.GetTodayQuizResponse;
import com.ggamja.domain.todayquiz.entity.TodayQuiz;
import com.ggamja.domain.todayquiz.repository.TodayQuizRepository;
import com.ggamja.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.ggamja.global.response.status.BaseExceptionResponseStatus.*;

@Service
@RequiredArgsConstructor
@Transactional
public class TodayQuizService {

    private final TodayQuizRepository todayQuizRepository;

    @Transactional(readOnly = true)
    public GetTodayQuizResponse getTodayQuiz() {
        LocalDate today = LocalDate.now();

        TodayQuiz todayQuiz = todayQuizRepository.findByDate(today)
                .orElseThrow(() -> new CustomException(TODAYQUIZ_NOT_FOUND));

        return GetTodayQuizResponse.from(todayQuiz.getQuiz());
    }
}
