package com.ggamja.global.response;

import com.ggamja.global.response.status.ResponseStatus;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@Getter
@JsonPropertyOrder({"code", "message", "data"})
public class BaseErrorResponse {
    private final int code;
    private final String message;
    private final Object data;

    public BaseErrorResponse(ResponseStatus status) {
        this.code = status.getCode();
        this.message = status.getMessage();
        this.data = null;
    }

    public BaseErrorResponse(ResponseStatus status, String message) {
        this.code = status.getCode();
        this.message = message;
        this.data = null;
    }
}
