package com.example.waggle.global.util;

import com.example.waggle.exception.object.general.GeneralException;
import com.example.waggle.exception.payload.code.ErrorStatus;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

import java.util.List;

public class ObjectUtil {
    public static boolean isPresent(@Nullable Object[] array) {
        return !ObjectUtils.isEmpty(array);
    }

    public static boolean isPresent(@Nullable Object object) {
        return !ObjectUtils.isEmpty(object);
    }

    public static void validateKeywordLength(String keyword) {
        if (keyword == null) {
            return;
        }
        if (keyword.length() < 2) {
            throw new GeneralException(ErrorStatus.BOARD_SEARCHING_KEYWORD_IS_TOO_SHORT);
        }
    }

    public static <T> boolean doesNotContain(List<T> objectList, T target) {
        return !objectList.contains(target);
    }
}
