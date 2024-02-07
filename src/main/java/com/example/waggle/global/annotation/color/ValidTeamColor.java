package com.example.waggle.global.annotation.color;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {ValidTeamColorValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTeamColor {
    String message() default "Invalid team color";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
