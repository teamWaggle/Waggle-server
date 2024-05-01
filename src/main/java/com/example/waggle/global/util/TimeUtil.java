package com.example.waggle.global.util;

import java.time.LocalDateTime;

public class TimeUtil {
    public static LocalDateTime nowWithoutNano() {
        return LocalDateTime.parse(LocalDateTime.now().toString().split("\\.")[0]);
    }
}
