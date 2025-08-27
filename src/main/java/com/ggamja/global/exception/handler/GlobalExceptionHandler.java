package com.ggamja.global.exception.handler;

import com.ggamja.global.exception.CustomException;
import com.ggamja.global.response.BaseErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<BaseErrorResponse> handleCustomException(CustomException e) {
        BaseErrorResponse response = new BaseErrorResponse(e.getStatus());
        return ResponseEntity
                .status(( e.getStatus()).getHttpStatus())
                .body(response);
    }


}

