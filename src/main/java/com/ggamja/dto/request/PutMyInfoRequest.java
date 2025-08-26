package com.ggamja.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record PutMyInfoRequest(
        @Schema(description = "새 닉네임", example = "깜자대장")
        String nickname,

        @Schema(description = "새 비밀번호", example = "newpassword123")
        String password,

        @Schema(description = "비밀번호 확인", example = "newpassword123")
        String passwordConfirm
) {}