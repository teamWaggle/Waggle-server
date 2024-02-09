package com.example.waggle.global.annotation.color;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ValidEnumValidator implements ConstraintValidator<ValidEnum, String> {
    private Set<String> enumValues;

    @Override
    public void initialize(ValidEnum targetEnum) {
        Class<? extends Enum> enumSelected = targetEnum.target();
        enumValues = (Set<String>) EnumSet.allOf(enumSelected).stream()
                .map(e -> ((Enum<? extends Enum<?>>) e).name()).collect(Collectors
                        .toSet());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return enumValues.contains(value);
    }
}
