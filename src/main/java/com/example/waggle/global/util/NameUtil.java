package com.example.waggle.global.util;

import java.util.UUID;

public class NameUtil {

    private static final String WAGGLE_AUTO_USERNAME = "waggle";
    private static final String WAGGLE_AUTO_NICKNAME = "nickname";
    public static String generateAutoUsername() {
        String prefix = WAGGLE_AUTO_USERNAME;
        String uniqueValue = UUID.randomUUID().toString().substring(0, 5).replace("-","#");

        StringBuilder usernameBuilder = new StringBuilder();
        usernameBuilder.append(prefix).append(uniqueValue);

        return usernameBuilder.toString();
    }

    public static String generateAutoNickname() {
        String prefix = WAGGLE_AUTO_NICKNAME;
        String uniqueValue = UUID.randomUUID().toString().substring(0, 5).replace("-","#");

        StringBuilder usernameBuilder = new StringBuilder();
        usernameBuilder.append(prefix).append(uniqueValue);

        return usernameBuilder.toString();

    }
}
