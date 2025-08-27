package com.ggamja.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.ggamja.global.response.status.ResponseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import static com.ggamja.global.response.status.BaseExceptionResponseStatus.*;

@Getter
@JsonPropertyOrder({"code", "message", "data"})
@Schema(description = "성공 응답")
public class BaseResponse<T> {

    @Schema(description = "응답 코드", example = "20000")
    private final int code;
    @Schema(description = "응답 메시지", example = "요청에 성공했습니다.")
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "응답 데이터")
    private final T data;

    public BaseResponse(T data) {
        this.code = SUCCESS.getCode();
        this.message = SUCCESS.getMessage();
        this.data = data;
    }

    public static <T> BaseResponse<T> ok(T data) {
        return new BaseResponse<>(data);
    }


}
