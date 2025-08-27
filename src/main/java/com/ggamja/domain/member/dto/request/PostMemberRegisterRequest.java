package com.ggamja.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record PostMemberRegisterRequest(

        @NotBlank
        @Size(min = 2, max = 15, message = "닉네임은 2~15자여야 합니다.")
        @Schema(description = "닉네임", example = "깜자대장")
        String nickname,

        @NotBlank
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        @Schema(description = "이메일", example = "test1@example.com")
        String email,

        @NotBlank
        @Schema(description = "비밀번호", example = "test1234!")
        String password,

        @NotBlank
        @Schema(description = "비밀번호 확인", example = "test1234!")
        String passwordConfirm
) {}

