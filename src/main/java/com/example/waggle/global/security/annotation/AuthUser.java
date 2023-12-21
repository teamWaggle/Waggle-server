package com.example.waggle.global.security.annotation;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthUser {

    boolean errorOnInvalidType() default true;
}
