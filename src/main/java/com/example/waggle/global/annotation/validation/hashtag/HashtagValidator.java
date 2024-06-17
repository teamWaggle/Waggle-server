package com.example.waggle.global.annotation.validation.hashtag;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.regex.Pattern;

public class HashtagValidator implements ConstraintValidator<ValidHashtag, List<String>> {

    private static final String HASHTAG_PATTERN = "^[a-zA-Z0-9가-힣]{1,8}$";

    @Override
    public void initialize(ValidHashtag constraintAnnotation) {
    }

    @Override
    public boolean isValid(List<String> hashtags, ConstraintValidatorContext context) {
        if (hashtags == null) {
            return true;
        }

        Pattern pattern = Pattern.compile(HASHTAG_PATTERN);
        for (String hashtag : hashtags) {
            if (!pattern.matcher(hashtag).matches()) {
                return false;
            }
        }
        return true;
    }
}