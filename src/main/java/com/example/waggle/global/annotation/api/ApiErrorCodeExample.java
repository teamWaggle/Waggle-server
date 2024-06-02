package com.example.waggle.global.annotation.api;

import com.example.waggle.exception.payload.code.ErrorStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.example.waggle.global.annotation.api.PredefinedErrorStatus.DEFAULT;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiErrorCodeExample {

    ErrorStatus[] value();

    PredefinedErrorStatus status() default DEFAULT;
}