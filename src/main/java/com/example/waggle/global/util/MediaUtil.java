package com.example.waggle.global.util;

import org.springframework.beans.factory.annotation.Value;

public class MediaUtil {
    @Value("${app.server.uri}")
    private static String SERVER_URI;

    public static String appendUri(String s3URI) {
        StringBuffer stringBuffer = new StringBuffer(SERVER_URI);
        return stringBuffer.append("/").append(s3URI).toString();
    }
}
