package com.ggamja.global.docs;

import com.ggamja.global.response.status.BaseExceptionResponseStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DocumentedApiErrors {
    BaseExceptionResponseStatus[] value();
}