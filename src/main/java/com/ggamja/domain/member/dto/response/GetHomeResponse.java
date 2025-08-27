package com.ggamja.domain.member.dto.response;

import com.ggamja.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;

public record GetHomeResponse(
        @Schema(description = "닉네임", example = "깜자돌이")
        String nickname,

        @Schema(description = "레벨", example = "3")
        String levelName,

        @Schema(description = "포인트", example = "120")
        int points,

        @Schema(description = "레벨 캐릭터 이미지 URL", example = "https://cdn.ggamja.com/level3.png")
        String characterUrl
) {
    public static GetHomeResponse of(Member member) {
        return new GetHomeResponse(
                member.getNickname(),
                member.getLevel().getLevel(),       // 레벨 번호/이름
                member.getPoints(),                 // 포인트
                member.getLevel().getCharacterUrl() // 캐릭터 이미지 URL
        );
    }
}