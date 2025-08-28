package com.ggamja.domain.analysis.service;

import com.ggamja.domain.analysis.dto.response.CategoryStat;
import com.ggamja.domain.analysis.dto.response.GetMonthlyCategoryAnalysisResponse;
import com.ggamja.domain.member.entity.Member;
import com.ggamja.domain.quizlog.repository.QuizLogRepository;
import com.ggamja.domain.studylog.repository.StudyLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AnalysisService {

    private final QuizLogRepository quizLogRepository;
    private final StudyLogRepository studyLogRepository;

    public GetMonthlyCategoryAnalysisResponse getMonthlyAnalysis(Member member) {
        LocalDateTime start = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime end = LocalDateTime.now();

        List<CategoryStat> categories =
                quizLogRepository.findMonthlyCategoryStats(member, start, end).stream()
                        .map(stat -> {
                            int accuracy = stat.getQuizLogCount() == 0 ? 0 :
                                    (int) ((double) stat.getCorrectCount() / stat.getQuizLogCount() * 100);

                            Long solvedCardCount = studyLogRepository
                                    .countSolvedCardsByCategoryThisMonth(member, stat.getCategory(), start, end);

                            return CategoryStat.of(
                                    stat.getCategory(),
                                    stat.getQuizLogCount(),
                                    solvedCardCount,
                                    accuracy
                            );
                        })
                        .sorted((a, b) -> Integer.compare(b.accuracy(), a.accuracy()))
                        .toList();

        int maxAcc = categories.stream().mapToInt(CategoryStat::accuracy).max().orElse(0);
        int minAcc = categories.stream().mapToInt(CategoryStat::accuracy).min().orElse(0);

        List<String> strengths = categories.stream()
                .filter(c -> c.accuracy() == maxAcc)
                .map(c -> c.category().name())
                .toList();

        List<String> weaknesses = categories.stream()
                .filter(c -> c.accuracy() == minAcc)
                .map(c -> c.category().name())
                .toList();

        boolean allEqual = (maxAcc == minAcc);

        return GetMonthlyCategoryAnalysisResponse.of(categories, strengths, weaknesses, allEqual);
    }

}
