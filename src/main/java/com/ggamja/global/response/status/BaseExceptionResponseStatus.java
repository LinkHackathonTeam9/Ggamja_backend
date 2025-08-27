package com.ggamja.global.response.status;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum BaseExceptionResponseStatus implements ResponseStatus {
    // 공통
    SUCCESS(20000, "요청에 성공했습니다.", HttpStatus.OK),
    BAD_REQUEST(40000, "유효하지 않은 요청입니다.", HttpStatus.BAD_REQUEST),
    NOT_FOUND(40400, "존재하지 않는 API입니다.", HttpStatus.NOT_FOUND),
    INTERNAL_SERVER_ERROR(50000, "서버 내부 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    AUTH_UNAUTHENTICATED(40100, "인증되지 않은 사용자입니다.", HttpStatus.UNAUTHORIZED),
    REQUIRED_FIELD_MISSING(40001, "필수 입력 항목이 누락되었습니다.", HttpStatus.BAD_REQUEST),

    // member
    INVALID_EMAIL_FORMAT(60000, "이메일 형식이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    DUPLICATED_EMAIL(60001, "이미 사용 중인 이메일입니다.", HttpStatus.BAD_REQUEST),
    PASSWORD_MISMATCH(60002, "비밀번호와 비밀번호 확인이 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    LEVEL_NOT_FOUND(60003, "기본 레벨이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    MEMBER_NOT_FOUND(60004, "회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    //card

    //todaycard

    //quiz

    //todayquiz
    QUIZ_NOT_FOUND(100000, "퀴즈를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    TODAYQUIZ_NOT_FOUND(100001, "오늘의 퀴즈를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    //studylog

    //quizlog

    ;

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
