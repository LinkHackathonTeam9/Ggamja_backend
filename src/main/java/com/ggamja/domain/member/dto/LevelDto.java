package com.ggamja.domain.member.dto;

import com.ggamja.domain.level.entity.Level;
import io.swagger.v3.oas.annotations.media.Schema;

public record LevelDto(
        @Schema(description = "레벨", example = "3")
        int level,

        @Schema(description = "레벨 시작 점수", example = "100")
        int startPoint,

        @Schema(description = "레벨 끝 점수 (다음 레벨 시작 점수)", example = "250")
        int endPoint,

        @Schema(description = "레벨 캐릭터 이름", example = "모험가 깜자쿤")
        String characterName,

        @Schema(description = "레벨 캐릭터 이미지 URL", example = "https://cdn.ggamja.com/level3.png")
        String characterUrl
) {
    public static LevelDto from(Level level, Level next) {
        int calculatedEndPoint = (next != null)
                ? next.getStartPoint()
                : level.getStartPoint();

        return new LevelDto(
                level.getLevel(),
                level.getStartPoint(),
                calculatedEndPoint,
                level.getCharacterName(),
                level.getCharacterUrl()
        );
    }
}