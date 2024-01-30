package com.example.waggle.global.security.annotation;

import io.swagger.v3.oas.annotations.Parameter;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthUser {

    boolean errorOnInvalidType() default true;

    @Parameter(hidden = true)
    boolean hidden() default true;
}
