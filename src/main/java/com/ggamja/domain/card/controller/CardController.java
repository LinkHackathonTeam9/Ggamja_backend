package com.ggamja.domain.card.controller;

import com.ggamja.domain.card.dto.response.GetCardDetailResponse;
import com.ggamja.domain.card.service.CardService;
import com.ggamja.domain.member.entity.Member;
import com.ggamja.global.docs.DocumentedApiErrors;
import com.ggamja.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.ggamja.global.response.status.BaseExceptionResponseStatus.CARD_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    @Operation(summary = "카드 상세 정보 조회", description = "카드 ID를 기반으로 상세 정보를 조회합니다. 조회 시 study_log에 기록됩니다.")
    @DocumentedApiErrors({ CARD_NOT_FOUND })
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<GetCardDetailResponse>> getCardDetail(
            @Parameter(description = "카드 ID", example = "1")
            @PathVariable Long id,
            @AuthenticationPrincipal Member member
    ) {
        GetCardDetailResponse response = cardService.getCardDetail(id, member);
        return ResponseEntity.ok(BaseResponse.ok(response));
    }
}

