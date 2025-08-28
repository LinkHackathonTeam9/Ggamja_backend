package com.ggamja.domain.todaycard.controller;

import com.ggamja.domain.card.dto.response.GetCardDetailResponse;
import com.ggamja.domain.card.entity.Category;
import com.ggamja.domain.member.entity.Member;
import com.ggamja.domain.todaycard.dto.response.GetTodayCardCategoriesResponse;
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

    @Operation(summary = "오늘의 카드 상세 정보 조회", description = "오늘 날짜와 카테고리(enum 이름)를 기준으로 오늘의 카드를 조회합니다. ")
    @DocumentedApiErrors({ INVALID_CATEGORY, CARD_NOT_FOUND })
    @GetMapping("/{category}")
    public ResponseEntity<BaseResponse<GetCardDetailResponse>> getTodayCardDetail(
            @Parameter(description = "카테고리: CAPITAL, PROVERB, ENGLISH, SCIENCE,HISTORY,COMMON_KNOWLEDGE)", example = "CAPITAL")
            @PathVariable Category category,
            @AuthenticationPrincipal Member member
    ) {
        GetCardDetailResponse response = todayCardService.getTodayCardDetail(category, member);
        return ResponseEntity.ok(BaseResponse.ok(response));
    }

    @Operation(summary = "오늘의 카드 학습한 카테고리 조회", description = "학습 완료한 오늘의 카드 카테고리 목록을 조회합니다.")
    @DocumentedApiErrors({ AUTH_UNAUTHENTICATED })
    @GetMapping("/category")
    public ResponseEntity<BaseResponse<GetTodayCardCategoriesResponse>> getTodayCardCategories(
            @AuthenticationPrincipal Member member
    ) {
        GetTodayCardCategoriesResponse response = todayCardService.getTodayCategories(member);
        return ResponseEntity.ok(BaseResponse.ok(response));
    }
}