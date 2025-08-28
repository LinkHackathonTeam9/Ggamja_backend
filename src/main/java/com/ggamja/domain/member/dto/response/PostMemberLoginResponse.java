package com.ggamja.domain.member.dto.response;

import com.ggamja.domain.level.entity.Level;
import com.ggamja.domain.member.dto.LevelDto;
import com.ggamja.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;

public record PostMemberLoginResponse(
        @Schema(description = "회원 ID", example = "1")
        Long id,

        @Schema(description = "닉네임", example = "깜자대장")
        String nickname,

        @Schema(description = "출석 보너스 지급 여부", example = "true")
        boolean bonusGiven,

        @Schema(description = "레벨 상세 정보")
        LevelDto level,

        @Schema(description = "레벨 변경 여부", example = "false")
        boolean levelChanged
) {
    public static PostMemberLoginResponse of(Member member, boolean bonusGiven, boolean levelChanged, Level next) {
        return new PostMemberLoginResponse(
                member.getId(),
                member.getNickname(),
                bonusGiven,
                LevelDto.from(member.getLevel(), next),
                levelChanged
        );
    }
}