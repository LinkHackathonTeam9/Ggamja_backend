package com.ggamja.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record PostMemberRegisterResponse(
        @Schema(description = "회원 ID", example = "1")
        Long id,

        @Schema(description = "닉네임", example = "깜자대장")
        String nickname
) {}
