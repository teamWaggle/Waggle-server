package com.example.waggle.global.annotation;

import com.example.waggle.global.payload.code.ErrorStatus;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiErrorCodeExample {

    ErrorStatus[] value();

}