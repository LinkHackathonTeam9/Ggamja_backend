package com.ggamja.domain.todayquiz.scheduler;

import com.ggamja.domain.quiz.entity.Quiz;
import com.ggamja.domain.quiz.repository.QuizRepository;
import com.ggamja.domain.todaycard.entity.TodayCard;
import com.ggamja.domain.todaycard.repository.TodayCardRepository;
import com.ggamja.domain.todayquiz.entity.TodayQuiz;
import com.ggamja.domain.todayquiz.repository.TodayQuizRepository;
import com.ggamja.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import static com.ggamja.global.response.status.BaseExceptionResponseStatus.QUIZ_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class TodayQuizScheduler {
    private final TodayCardRepository todayCardRepository;
    private final QuizRepository quizRepository;
    private final TodayQuizRepository todayQuizRepository;
    private static final Random RANDOM = new Random();

    @Scheduled(cron = "0 5 0 * * ?", zone = "Asia/Seoul")
    public void createDailyQuiz() {
        LocalDate today = LocalDate.now();

        if (todayQuizRepository.findByDate(today).isPresent()) {
            return;
        }

        List<TodayCard> todayCards = todayCardRepository.findAll();

        List<Quiz> quizzes = todayCards.stream()
                .flatMap(todayCard -> quizRepository.findByCard(todayCard.getCard()).stream())
                .toList();

        if (quizzes.isEmpty()) {
            throw new CustomException(QUIZ_NOT_FOUND);
        }

        Quiz selectedQuiz = quizzes.get(RANDOM.nextInt(quizzes.size()));

        TodayQuiz todayQuiz = TodayQuiz.builder()
                .date(today)
                .quiz(selectedQuiz)
                .build();

        todayQuizRepository.save(todayQuiz);
    }
}
