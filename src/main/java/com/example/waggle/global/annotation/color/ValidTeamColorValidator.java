package com.example.waggle.global.annotation.color;

import com.example.waggle.domain.schedule.entity.TeamColor;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidTeamColorValidator implements ConstraintValidator<ValidTeamColor, String> {
    @Override
    public void initialize(ValidTeamColor constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        for (TeamColor color : TeamColor.values()) {
            if (color.name().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
