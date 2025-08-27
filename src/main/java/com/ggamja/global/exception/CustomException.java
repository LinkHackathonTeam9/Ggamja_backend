package com.ggamja.global.exception;

import com.ggamja.global.response.status.ResponseStatus;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ResponseStatus exceptionStatus;

    public CustomException(ResponseStatus status) {
        super(status.getMessage());
        this.exceptionStatus = status;
    }

    public ResponseStatus getStatus() {
        return exceptionStatus;
    }
}
