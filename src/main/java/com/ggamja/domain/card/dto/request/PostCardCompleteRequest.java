package com.ggamja.domain.card.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record PostCardCompleteRequest(
        @Schema(description = "완료한 카드 ID", example = "1")
        Long cardId
) {}