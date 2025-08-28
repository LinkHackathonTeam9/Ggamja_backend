package com.ggamja.domain.card.service;

import com.ggamja.domain.card.dto.response.GetCardDetailResponse;
import com.ggamja.domain.card.entity.Card;
import com.ggamja.domain.card.repository.CardRepository;
import com.ggamja.domain.member.entity.Member;
import com.ggamja.domain.studylog.entity.StudyLog;
import com.ggamja.domain.studylog.repository.StudyLogRepository;
import com.ggamja.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ggamja.global.response.status.BaseExceptionResponseStatus.CARD_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final StudyLogRepository studyLogRepository;

    public GetCardDetailResponse getCardDetail(Long id, Member member) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new CustomException(CARD_NOT_FOUND));

        // 공부 이력 저장
        StudyLog log = StudyLog.builder()
                .member(member)
                .card(card)
                .build();
        studyLogRepository.save(log);

        return GetCardDetailResponse.builder()
                .id(card.getId())
                .category(card.getCategory())
                .title(card.getTitle())
                .meaning(card.getMeaning())
                .difficulty(card.getDifficulty())
                .build();
    }}
