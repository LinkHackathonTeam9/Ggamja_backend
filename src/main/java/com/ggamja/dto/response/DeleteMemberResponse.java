package com.ggamja.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record DeleteMemberResponse(
        @Schema(description = "탈퇴 완료 메시지", example = "회원 탈퇴가 완료되었습니다.")
        String message
) {
    public static DeleteMemberResponse success() {
        return new DeleteMemberResponse("회원 탈퇴가 완료되었습니다.");
    }
}

