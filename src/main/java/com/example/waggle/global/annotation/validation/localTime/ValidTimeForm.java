package com.example.waggle.global.annotation.validation.localTime;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {LocalTimeValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTimeForm {
    String message() default "The time format is not valid (expected format HH:mm)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
