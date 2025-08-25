package com.ggamja.exception;

public class MemberException extends RuntimeException {
    private final MemberExceptionResponseStatus status;

    public MemberException(MemberExceptionResponseStatus status) {
        super(status.getMessage());
        this.status = status;
    }

    public MemberExceptionResponseStatus getStatus() {
        return status;
    }
}

