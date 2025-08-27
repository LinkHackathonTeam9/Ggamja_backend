package com.ggamja.domain.member.dto.request;

import jakarta.validation.constraints.*;

public record PostMemberRegisterRequest(

        @NotBlank
        @Size(min = 2, max = 15, message = "닉네임은 2~15자여야 합니다.")
        String nickname,

        @NotBlank
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String email,

        @NotBlank
        String password,

        @NotBlank
        String passwordConfirm
) {}

