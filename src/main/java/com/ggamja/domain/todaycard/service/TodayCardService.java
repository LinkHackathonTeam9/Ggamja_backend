package com.ggamja.domain.todaycard.service;

import com.ggamja.domain.card.dto.response.GetCardDetailResponse;
import com.ggamja.domain.card.entity.Category;
import com.ggamja.domain.member.entity.Member;
import com.ggamja.domain.studylog.entity.StudyLog;
import com.ggamja.domain.studylog.repository.StudyLogRepository;
import com.ggamja.domain.todaycard.entity.TodayCard;
import com.ggamja.domain.todaycard.repository.TodayCardRepository;
import com.ggamja.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.ggamja.global.response.status.BaseExceptionResponseStatus.*;

@Service
@RequiredArgsConstructor
public class TodayCardService {

    private final TodayCardRepository todayCardRepository;
    private final StudyLogRepository studyLogRepository;

    public GetCardDetailResponse getTodayCardDetail(int categoryOrdinal, Member member) {
        LocalDate today = LocalDate.now();

        Category[] categories = Category.values();
        if (categoryOrdinal < 0 || categoryOrdinal >= categories.length) {
            throw new CustomException(INVALID_CATEGORY);
        }
        Category category = categories[categoryOrdinal];

        TodayCard todayCard = todayCardRepository
                .findByDateAndCard_Category(today, category)
                .orElseThrow(() -> new CustomException(CARD_NOT_FOUND));

        // 공부 이력 저장
        StudyLog log = StudyLog.builder()
                .member(member)
                .card(todayCard.getCard())
                .build();
        studyLogRepository.save(log);

        return GetCardDetailResponse.builder()
                .id(todayCard.getCard().getId())
                .category(todayCard.getCard().getCategory())
                .title(todayCard.getCard().getTitle())
                .meaning(todayCard.getCard().getMeaning())
                .difficulty(todayCard.getCard().getDifficulty())
                .build();
    }
}