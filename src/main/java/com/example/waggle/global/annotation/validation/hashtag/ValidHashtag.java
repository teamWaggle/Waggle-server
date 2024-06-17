package com.example.waggle.global.annotation.validation.hashtag;

import jakarta.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.messaging.handler.annotation.Payload;

@Constraint(validatedBy = HashtagValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidHashtag {
    String message() default "해시태그는 공백을 포함하지 않으며, 1자 이상 8자 이하의 특수문자를 포함하지 않는 문자열이어야 합니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}