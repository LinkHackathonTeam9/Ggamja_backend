package com.ggamja.domain.todaycard.controller;

import com.ggamja.domain.card.dto.response.GetCardDetailResponse;
import com.ggamja.domain.member.entity.Member;
import com.ggamja.domain.todaycard.service.TodayCardService;
import com.ggamja.global.docs.DocumentedApiErrors;
import com.ggamja.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.ggamja.global.response.status.BaseExceptionResponseStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/todaycards")
public class TodayCardController {

    private final TodayCardService todayCardService;

    @Operation(summary = "오늘의 카드 상세 정보 조회", description = "오늘 날짜와 카테고리 번호를 기준으로 오늘의 카드를 조회합니다. 조회 시 study_log에 기록됩니다.")
    @DocumentedApiErrors({ INVALID_CATEGORY, CARD_NOT_FOUND })
    @GetMapping("/{category}")
    public ResponseEntity<BaseResponse<GetCardDetailResponse>> getTodayCardDetail(
            @Parameter(description = "카테고리 번호 (0: CAPITAL, 1: PROVERB, 2: ENGLISH ...)", example = "0")
            @PathVariable int category,
            @AuthenticationPrincipal Member member
    ) {
        GetCardDetailResponse response = todayCardService.getTodayCardDetail(category, member);
        return ResponseEntity.ok(BaseResponse.ok(response));
    }
}