package com.ggamja.exception;

public enum MemberExceptionResponseStatus {
    DUPLICATED_EMAIL(60001, "이미 사용 중인 이메일입니다."),
    PASSWORD_MISMATCH(60002, "비밀번호와 비밀번호 확인이 일치하지 않습니다."),
    LEVEL_NOT_FOUND(60003, "기본 레벨이 존재하지 않습니다.");

    private final int code;
    private final String message;

    MemberExceptionResponseStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}