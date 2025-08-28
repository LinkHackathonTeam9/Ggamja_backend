package com.ggamja.domain.card.controller;

import com.ggamja.domain.attendance.entity.Attendance;
import com.ggamja.domain.card.dto.response.GetCardDetailResponse;
import com.ggamja.domain.card.dto.response.PostCardCompleteResponse;
import com.ggamja.domain.card.service.CardService;
import com.ggamja.domain.member.entity.Member;
import com.ggamja.global.docs.DocumentedApiErrors;
import com.ggamja.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.ggamja.global.response.status.BaseExceptionResponseStatus.AUTH_UNAUTHENTICATED;
import static com.ggamja.global.response.status.BaseExceptionResponseStatus.CARD_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cards")
@Tag(name = "Card", description = "CardController - 카드 관련 api")
public class CardController {

    private final CardService cardService;

    @Operation(
            summary = "카드 상세 정보 조회",
            description = "카드 ID를 기반으로 상세 정보를 조회합니다."
    )
    @DocumentedApiErrors({ CARD_NOT_FOUND })
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<GetCardDetailResponse>> getCardDetail(
            @Parameter(description = "카드 ID", example = "1")
            @PathVariable Long id
    ) {
        GetCardDetailResponse response = cardService.getCardDetail(id);
        return ResponseEntity.ok(BaseResponse.ok(response));
    }

    @Operation(
            summary = "학습 완료",
            description = "study_log에 학습 기록 등록 후 사용자의 포인트에 3점을 추가합니다."
    )
    @DocumentedApiErrors({ CARD_NOT_FOUND, AUTH_UNAUTHENTICATED })
    @PostMapping("/complete/{cardId}")
    public ResponseEntity<BaseResponse<PostCardCompleteResponse>> completeCard(
            @AuthenticationPrincipal Member member,
            @Parameter(description = "완료한 카드 ID", example = "1")
            @PathVariable Long cardId
    ) {
        PostCardCompleteResponse response = cardService.completeCard(member, cardId);
        return ResponseEntity.ok(BaseResponse.ok(response));
    }
}

