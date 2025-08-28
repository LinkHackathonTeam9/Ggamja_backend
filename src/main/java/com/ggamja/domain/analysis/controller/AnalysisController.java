package com.ggamja.domain.analysis.controller;

import com.ggamja.domain.analysis.dto.response.GetMonthlyCategoryAnalysisResponse;
import com.ggamja.domain.analysis.service.AnalysisService;
import com.ggamja.domain.member.entity.Member;
import com.ggamja.global.docs.DocumentedApiErrors;
import com.ggamja.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ggamja.global.response.status.BaseExceptionResponseStatus.AUTH_UNAUTHENTICATED;

@RestController
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
@Tag(name = "Analysis", description = "AnalysisController - 통계 관련 api")
public class AnalysisController {

    private final AnalysisService analysisService;

    @Operation(
            summary = "카테고리별 월간 분석 조회",
            description = "한 달 동안의 카테고리별 퀴즈 풀이 수, 학습한 카드 수, 정답률을 조회하고 강점/약점 카테고리를 도출합니다."
    )
    @DocumentedApiErrors({AUTH_UNAUTHENTICATED})
    @GetMapping("/categories/monthly")
    public ResponseEntity<BaseResponse<GetMonthlyCategoryAnalysisResponse>> getMonthlyAnalysis(
            @AuthenticationPrincipal Member member
    ) {

        return ResponseEntity.ok(BaseResponse.ok(analysisService.getMonthlyAnalysis(member)));
    }
}
