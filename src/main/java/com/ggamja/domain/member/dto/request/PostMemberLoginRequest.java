package com.ggamja.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

public record PostMemberLoginRequest(
        @NotBlank
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        @Schema(description = "이메일", example = "test1@example.com")
        String email,

        @NotBlank
        @Schema(description = "비밀번호", example = "test1234!")
        String password
) {}