package com.ggamja.domain.member.dto.response;

import com.ggamja.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;

public record PutMyInfoResponse(
        @Schema(description = "회원 ID", example = "1")
        Long id,

        @Schema(description = "닉네임", example = "깜자대장")
        String nickname,

        @Schema(description = "이메일", example = "test@example.com")
        String email
) {
    public static PutMyInfoResponse of(Member member) {
        return new PutMyInfoResponse(
                member.getId(),
                member.getNickname(),
                member.getEmail()
        );
    }
}