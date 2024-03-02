package com.example.waggle.global.util;

import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

public class ObjectUtil {
    public static boolean isPresent(@Nullable Object[] array) {
        return !ObjectUtils.isEmpty(array);
    }

    public static boolean isPresent(@Nullable Object object) {
        return !ObjectUtils.isEmpty(object);
    }
}
