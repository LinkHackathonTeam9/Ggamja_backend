package com.ggamja.domain.todaycard.scheduler;

import com.ggamja.domain.card.entity.Card;
import com.ggamja.domain.card.entity.Category;
import com.ggamja.domain.card.repository.CardRepository;
import com.ggamja.domain.todaycard.entity.TodayCard;
import com.ggamja.domain.todaycard.repository.TodayCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TodayCardScheduler {

    private final CardRepository cardRepository;
    private final TodayCardRepository todayCardRepository;
    private static final Random RANDOM = new Random();

    // 매일 00:02 → 카테고리별 오늘의카드 선정
    @Scheduled(cron = "0 15 1 * * ?", zone = "Asia/Seoul")
    public void createDailyTodayCards() {
        LocalDate today = LocalDate.now();

        // 이미 오늘의카드가 선정됐다면 중복 생성 방지
        if (!todayCardRepository.findByDate(today).isEmpty()) {
            return;
        }

        YearMonth thisMonth = YearMonth.from(today);
        LocalDate start = thisMonth.atDay(1);
        LocalDate end = thisMonth.atEndOfMonth();

        for (Category category : Category.values()) {
            // 이번 달에 이미 뽑힌 카드 목록
            Set<Long> usedCardIds = todayCardRepository.findByMonthAndCategory(
                            start, end, category
                    ).stream()
                    .map(tc -> tc.getCard().getId())
                    .collect(Collectors.toSet());

            // 후보 카드 목록
            List<Card> candidates = cardRepository.findByCategory(category).stream()
                    .filter(card -> !usedCardIds.contains(card.getId()))
                    .toList();

            if (candidates.isEmpty()) {
                // 모든 카드를 이미 소진한 경우: 그냥 전체 카드에서 다시 랜덤
                candidates = cardRepository.findByCategory(category);
            }

            // 랜덤 선택
            Card selected = candidates.get(RANDOM.nextInt(candidates.size()));

            // 저장
            TodayCard todayCard = TodayCard.builder()
                    .date(today)
                    .card(selected)
                    .build();

            todayCardRepository.save(todayCard);
        }
    }

    // 매월 1일 00:00 → 테이블 초기화
    @Scheduled(cron = "0 0 0 1 * ?")
    public void resetMonthlyTodayCards() {
        todayCardRepository.deleteAll();
    }
}