package com.ggamja.controller;

import com.ggamja.exception.MemberException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<Map<String, Object>> handleMemberException(MemberException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("code", ex.getStatus().getCode());
        body.put("message", ex.getStatus().getMessage());
        return ResponseEntity.badRequest().body(body);
    }
}

