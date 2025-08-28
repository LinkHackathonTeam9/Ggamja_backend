package com.ggamja.domain.member.dto.response;

import com.ggamja.domain.level.entity.Level;
import com.ggamja.domain.member.dto.LevelDto;
import com.ggamja.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;

public record GetMyInfoResponse(
        @Schema(description = "회원 ID", example = "1")
        Long id,

        @Schema(description = "닉네임", example = "깜자돌이")
        String nickname,

        @Schema(description = "이메일", example = "test@example.com")
        String email,

        @Schema(description = "포인트", example = "120")
        int points,

        @Schema(description = "레벨 상세 정보")
        LevelDto level
) {
    public static GetMyInfoResponse of(Member member, Level next) {
        return new GetMyInfoResponse(
                member.getId(),
                member.getNickname(),
                member.getEmail(),
                member.getPoints(),
                LevelDto.from(member.getLevel(), next)
        );
    }
}