package com.ggamja.domain.member.dto.response;

import com.ggamja.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;

public record PostMemberLoginResponse(
        @Schema(description = "회원 ID", example = "1")
        Long id,

        @Schema(description = "닉네임", example = "깜자대장")
        String nickname,

        @Schema(description = "레벨 이름", example = "1")
        String levelName,

        @Schema(description = "출석 보너스 지급 여부", example = "true")
        boolean bonusGiven,

        @Schema(description = "레벨 변경 여부", example = "false")
        boolean levelChanged
) {
    public static PostMemberLoginResponse of(Member member, boolean bonusGiven, boolean levelChanged) {
        return new PostMemberLoginResponse(
                member.getId(),
                member.getNickname(),
                member.getLevel().getLevel(),
                bonusGiven,
                levelChanged
        );
    }
}