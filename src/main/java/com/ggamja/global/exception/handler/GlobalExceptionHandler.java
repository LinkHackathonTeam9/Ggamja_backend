package com.ggamja.global.exception.handler;

import com.ggamja.global.exception.CustomException;
import com.ggamja.global.response.BaseErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.apache.coyote.BadRequestException;
import org.hibernate.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import static com.ggamja.global.response.status.BaseExceptionResponseStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<BaseErrorResponse> handle_CustomException(CustomException e) {
        BaseErrorResponse response = new BaseErrorResponse(e.getStatus());
        return ResponseEntity
                .status(( e.getStatus()).getHttpStatus())
                .body(response);
    }

    // 잘못된 요청일 경우
    @ExceptionHandler({BadRequestException.class, TypeMismatchException.class, MissingServletRequestParameterException.class})
    public ResponseEntity<BaseErrorResponse> handle_BadRequest(Exception e){
        return ResponseEntity.badRequest().body(new BaseErrorResponse(BAD_REQUEST));
    }

    // 요청한 api가 없을 경우
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<BaseErrorResponse> handle_NoHandlerFoundException(Exception e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseErrorResponse(NOT_FOUND));
    }

    // 런타임 오류가 발생한 경우
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<BaseErrorResponse> handle_RuntimeException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BaseErrorResponse(INTERNAL_SERVER_ERROR));
    }

    // RequestParam, PathVariable 등의 validation 실패
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<BaseErrorResponse> handle_ConstraintViolation(Exception e) {
        return ResponseEntity.badRequest().body(new BaseErrorResponse(REQUIRED_FIELD_MISSING));
    }

    // DTO 검증 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseErrorResponse> handle_Validation(MethodArgumentNotValidException e) {
        FieldError error = e.getBindingResult().getFieldError();
        if (error != null) {
            String errorCode = error.getCode(); // ex: "NotBlank", "Email" 등

            if ("Email".equals(errorCode)) {
                return ResponseEntity.badRequest().body(new BaseErrorResponse(INVALID_EMAIL_FORMAT));
            }
        }
        return ResponseEntity.badRequest().body(new BaseErrorResponse(REQUIRED_FIELD_MISSING));
    }

}

