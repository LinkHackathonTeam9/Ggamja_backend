package com.ggamja.domain.member.dto.response;

import com.ggamja.domain.level.entity.Level;
import com.ggamja.domain.member.dto.LevelDto;
import com.ggamja.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;

public record GetHomeResponse(
        @Schema(description = "닉네임", example = "깜자돌이")
        String nickname,

        @Schema(description = "포인트", example = "120")
        int points,

        @Schema(description = "레벨 상세 정보")
        LevelDto level
) {
    public static GetHomeResponse of(Member member) {
        return new GetHomeResponse(
                member.getNickname(),
                member.getPoints(),
                LevelDto.from(member.getLevel())
        );
    }

}