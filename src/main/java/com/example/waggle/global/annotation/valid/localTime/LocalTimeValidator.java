package com.example.waggle.global.annotation.valid.localTime;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import static com.example.waggle.global.util.ScheduleUtil.TIME_FORMATTER;

@Slf4j
@Component
public class LocalTimeValidator implements ConstraintValidator<ValidTimeForm, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return false;
        }
        try {
            log.info("valid parse");
            LocalTime.parse(value, TIME_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            log.info("can't");
            return false;
        }
    }
}
