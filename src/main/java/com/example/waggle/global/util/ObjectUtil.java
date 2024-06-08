package com.example.waggle.global.util;

import com.example.waggle.exception.object.general.GeneralException;
import com.example.waggle.exception.payload.code.ErrorStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

import java.util.List;

public class ObjectUtil {
    private static final int MIN_KEYWORD_LENGTH = 2;

    public static boolean isPresent(@Nullable Object[] array) {
        return !ObjectUtils.isEmpty(array);
    }

    public static boolean isPresent(@Nullable Object object) {
        return !ObjectUtils.isEmpty(object);
    }

    public static void validateKeywordLength(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return;
        }
        if (keyword.length() < MIN_KEYWORD_LENGTH) {
            throw new GeneralException(ErrorStatus.SEARCHING_KEYWORD_IS_TOO_SHORT);
        }
    }

    public static <T> boolean doesNotContain(List<T> objectList, T target) {
        return !objectList.contains(target);
    }

    public static String serialize(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = null;
        try {
            result = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
