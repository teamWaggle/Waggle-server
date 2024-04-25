package com.example.waggle.global.util;

import org.springframework.data.domain.Page;

public class PageUtil {
    public static int THIS_PAGE_IS_LAST = -1;
    public static int STORY_SIZE = 9;
    public static int QUESTION_SIZE = 9;
    public static int COMMENT_SIZE = 5;
    public static int TEAM_RECOMMEND_SIZE = 3;

    public static int countNextPage(Page<?> pagedList) {
        if (pagedList.isLast()) return THIS_PAGE_IS_LAST;
        return pagedList.getNumber() + 1;
    }
}
