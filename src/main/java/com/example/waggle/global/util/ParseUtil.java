package com.example.waggle.global.util;

import com.example.waggle.domain.conversation.persistence.entity.Conversation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseUtil {

    private static final String mentionRegex = "@\\[([^]]+)]\\(([^)]+)\\)";

    public static List<String> parsingUserUrl(Conversation conversation) {
        Pattern pattern = Pattern.compile(mentionRegex);
        Matcher matcher = pattern.matcher(conversation.getContent());
        List<String> userUrlList = new ArrayList<>();
        while (matcher.find()) {
            userUrlList.add(matcher.group(2));
        }
        return userUrlList;
    }
}
