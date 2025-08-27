package com.ggamja.domain.todayquiz.service;

import com.ggamja.domain.member.entity.Member;
import com.ggamja.domain.member.repository.MemberRepository;
import com.ggamja.domain.quiz.entity.Quiz;
import com.ggamja.domain.quizlog.entity.QuizLog;
import com.ggamja.domain.quizlog.repository.QuizLogRepository;
import com.ggamja.domain.todayquiz.dto.request.PostTodayQuizSubmitRequest;
import com.ggamja.domain.todayquiz.dto.response.GetTodayQuizResponse;
import com.ggamja.domain.todayquiz.dto.response.PostTodayQuizSubmitResponse;
import com.ggamja.domain.todayquiz.entity.TodayQuiz;
import com.ggamja.domain.todayquiz.repository.TodayQuizRepository;
import com.ggamja.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.ggamja.global.response.status.BaseExceptionResponseStatus.*;

@Service
@RequiredArgsConstructor
@Transactional
public class TodayQuizService {

    private final TodayQuizRepository todayQuizRepository;
    private final QuizLogRepository quizLogRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public GetTodayQuizResponse getTodayQuiz() {
        LocalDate today = LocalDate.now();

        TodayQuiz todayQuiz = todayQuizRepository.findByDate(today)
                .orElseThrow(() -> new CustomException(TODAYQUIZ_NOT_FOUND));

        return GetTodayQuizResponse.from(todayQuiz);
    }

    public PostTodayQuizSubmitResponse submitTodayQuiz(Member member, Long todayQuizId, PostTodayQuizSubmitRequest request) {
        TodayQuiz todayQuiz = todayQuizRepository.findById(todayQuizId)
                .orElseThrow(() -> new CustomException(TODAYQUIZ_NOT_FOUND));

        Quiz quiz = todayQuiz.getQuiz();
        boolean isCorrect = quiz.getAnswer().trim().equalsIgnoreCase(request.answer().trim());

        QuizLog quizLog = QuizLog.builder()
                .date(LocalDateTime.now())
                .member(member)
                .quiz(quiz)
                .selectedAnswer(request.answer())
                .isCorrect(isCorrect)
                .build();

        quizLogRepository.save(quizLog);

        if (isCorrect) {
            member.addPoints(quiz.getPoint());
            memberRepository.save(member);
        }

        return PostTodayQuizSubmitResponse.from(todayQuiz, isCorrect);
    }
}
