package com.ggamja.domain.card.service;

import com.ggamja.domain.card.dto.response.GetCardDetailResponse;
import com.ggamja.domain.card.dto.response.PostCardCompleteResponse;
import com.ggamja.domain.card.entity.Card;
import com.ggamja.domain.card.repository.CardRepository;
import com.ggamja.domain.member.entity.Member;
import com.ggamja.domain.member.repository.MemberRepository;
import com.ggamja.domain.studylog.entity.StudyLog;
import com.ggamja.domain.studylog.repository.StudyLogRepository;
import com.ggamja.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ggamja.global.response.status.BaseExceptionResponseStatus.CARD_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final StudyLogRepository studyLogRepository;
    private final MemberRepository memberRepository;

    public GetCardDetailResponse getCardDetail(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new CustomException(CARD_NOT_FOUND));

        return GetCardDetailResponse.builder()
                .id(card.getId())
                .category(card.getCategory())
                .title(card.getTitle())
                .meaning(card.getMeaning())
                .difficulty(card.getDifficulty())
                .build();
    }

    @Transactional
    public PostCardCompleteResponse completeCard(Member member, Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CustomException(CARD_NOT_FOUND));

        // study_log 기록 추가
        StudyLog log = StudyLog.builder()
                .member(member)
                .card(card)
                .build();
        studyLogRepository.save(log);

        // 포인트 +3점
        member.setPoints(member.getPoints() + 3);
        memberRepository.save(member);

        return new PostCardCompleteResponse(member.getPoints());
    }
}
