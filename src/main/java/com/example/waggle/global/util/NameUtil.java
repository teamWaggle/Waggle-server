package com.example.waggle.global.util;

import com.example.waggle.global.exception.handler.MemberHandler;
import com.example.waggle.global.payload.code.ErrorStatus;

import java.util.UUID;

public class NameUtil {

    private static final String WAGGLE_AUTO_USERNAME = "user#";
    private static final String WAGGLE_AUTO_USERURL = "waggle";
    private static final String WAGGLE_AUTO_NICKNAME = "nickname";

    public enum NameType {
        USERNAME, NICKNAME, USERURL
    }

    public static String generateAuto(NameType nameType) {
        String prefix;
        switch (nameType) {
            case USERURL:
                prefix = WAGGLE_AUTO_USERURL;
                break;
            case NICKNAME:
                prefix = WAGGLE_AUTO_NICKNAME;
                break;
            case USERNAME:
                prefix = WAGGLE_AUTO_USERNAME;
                break;
            default:
                throw new MemberHandler(ErrorStatus.MEMBER_NAME_TYPE_IS_NOT_FOUND);
        }
        StringBuilder usernameBuilder = getStringBuilder(prefix);

        return usernameBuilder.toString();
    }

    private static StringBuilder getStringBuilder(String prefix) {
        String uniqueValue = UUID.randomUUID().toString().substring(0, 5).replace("-", "#");

        StringBuilder usernameBuilder = new StringBuilder();
        usernameBuilder.append(prefix).append(uniqueValue);
        return usernameBuilder;
    }

}
