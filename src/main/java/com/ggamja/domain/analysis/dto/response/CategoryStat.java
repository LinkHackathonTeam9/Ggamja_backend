package com.ggamja.domain.analysis.dto.response;

import com.ggamja.domain.card.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;

public record CategoryStat(
        @Schema(description = "카테고리", example = "HISTORY")
        Category category,

        @Schema(description = "풀이한 퀴즈 개수", example = "12")
        int quizLogCount,

        @Schema(description = "학습한 카드 개수", example = "10")
        int cardLogCount,

        @Schema(description = "퀴즈 정답률(%)", example = "80")
        int accuracy
) {
    public static CategoryStat of(Category category, Long quizLogCount, long cardLogCount, int accuracy) {
        return new CategoryStat(category, quizLogCount.intValue(), (int) cardLogCount, accuracy);
    }
}
