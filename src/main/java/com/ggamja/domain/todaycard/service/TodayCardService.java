package com.ggamja.domain.todaycard.service;

import com.ggamja.domain.card.dto.response.GetCardDetailResponse;
import com.ggamja.domain.card.entity.Category;
import com.ggamja.domain.todaycard.entity.TodayCard;
import com.ggamja.domain.todaycard.repository.TodayCardRepository;
import com.ggamja.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.ggamja.global.response.status.BaseExceptionResponseStatus.CARD_NOT_FOUND;
import static com.ggamja.global.response.status.BaseExceptionResponseStatus.INVALID_CATEGORY;

@Service
@RequiredArgsConstructor
public class TodayCardService {

    private final TodayCardRepository todayCardRepository;

    public GetCardDetailResponse getTodayCardDetail(int categoryOrdinal) {
        LocalDate today = LocalDate.now();

        Category[] categories = Category.values();
        if (categoryOrdinal < 0 || categoryOrdinal >= categories.length) {
            throw new CustomException(INVALID_CATEGORY);
        }
        Category category = categories[categoryOrdinal];

        TodayCard todayCard = todayCardRepository
                .findByDateAndCard_Category(today, category)
                .orElseThrow(() -> new CustomException(CARD_NOT_FOUND));

        return GetCardDetailResponse.builder()
                .id(todayCard.getCard().getId())
                .category(todayCard.getCard().getCategory())
                .title(todayCard.getCard().getTitle())
                .meaning(todayCard.getCard().getMeaning())
                .difficulty(todayCard.getCard().getDifficulty())
                .build();
    }
}