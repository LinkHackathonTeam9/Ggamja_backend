package com.ggamja.domain.card.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record PostCardCompleteResponse(
        @Schema(description = "공부 로그 ID", example = "10")
        Long studyLogId,

        @Schema(description = "현재 포인트", example = "53")
        int currentPoints
) {}