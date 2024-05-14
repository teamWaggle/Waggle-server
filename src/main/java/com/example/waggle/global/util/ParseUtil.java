package com.example.waggle.global.util;

import com.example.waggle.domain.conversation.persistence.entity.Conversation;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class ParseUtil {

    private static final String MENTION_REGEX = "@\\[([^]]+)]\\(([^)]+)\\)";
    private static final Pattern MENTION_PATTERN = Pattern.compile(MENTION_REGEX);

    public static List<String> parsingUserUrl(Conversation conversation) {
        if (conversation == null || conversation.getContent() == null) {
            log.warn("NPE");
            return new ArrayList<>();
        }

        Matcher matcher = MENTION_PATTERN.matcher(conversation.getContent());
        List<String> userUrlList = new ArrayList<>();
        while (matcher.find()) {
            userUrlList.add(matcher.group(2));
        }
        return userUrlList;
    }
}
