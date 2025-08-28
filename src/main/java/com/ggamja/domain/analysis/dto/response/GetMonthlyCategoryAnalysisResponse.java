package com.ggamja.domain.analysis.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record GetMonthlyCategoryAnalysisResponse(
        @Schema(description = "사용자 닉네임", example = "채붕이")
        String nickname,

        @Schema(description = "카테고리별 통계 목록")
        List<CategoryStat> categories,

        @Schema(description = "정답률 상위 카테고리 리스트", example = "[\"HISTORY\"]")
        List<String> strengths,

        @Schema(description = "정답률 하위 카테고리 리스트", example = "[\"CAPITAL\"]")
        List<String> weaknesses,

        @Schema(description = "모든 카테고리 정답률 동일 여부", example = "false")
        boolean allEqual
) {
    public static GetMonthlyCategoryAnalysisResponse of(String nickname,
                                                        List<CategoryStat> categories,
                                                        List<String> strengths,
                                                        List<String> weaknesses,
                                                        boolean allEqual) {
        return new GetMonthlyCategoryAnalysisResponse(nickname, categories, strengths, weaknesses, allEqual);
    }
}