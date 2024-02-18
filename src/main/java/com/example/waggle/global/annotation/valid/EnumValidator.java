package com.example.waggle.global.annotation.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class EnumValidator implements ConstraintValidator<ValidEnum, String> {
    private Set<String> enumValues;

    @Override
    public void initialize(ValidEnum targetEnum) {
        enumValues = Arrays.stream(targetEnum.target().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || enumValues.contains(value);
    }

}
