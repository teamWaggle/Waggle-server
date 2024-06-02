package com.example.waggle.global.util;

import com.example.waggle.domain.board.persistence.entity.BoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public class PageUtil {
    public static int THIS_PAGE_IS_LAST = -1;
    public static int STORY_SIZE = 9;
    public static int QUESTION_SIZE = 9;
    public static int SIREN_SIZE = 8;
    public static int COMMENT_SIZE = 5;
    public static int CHAT_ROOM_SIZE = 9;
    public static int CHAT_MESSAGE_SIZE = 20;
    public static int TEAM_RECOMMEND_SIZE = 3;

    public static Sort LATEST_SORTING = Sort.by("createdDate").descending();
    public static Sort OLDEST_SORTING = Sort.by("createdDate").ascending();

    public static int countNextPage(Page<?> pagedList) {
        if (pagedList.isLast()) {
            return THIS_PAGE_IS_LAST;
        }
        return pagedList.getNumber() + 1;
    }

    public static Sort getCommentSortingMethod(BoardType boardType) {
        switch (boardType) {
            case STORY, SCHEDULE -> {
                return LATEST_SORTING;
            }
            case SIREN, QUESTION -> {
                return OLDEST_SORTING;
            }
            default -> {
                return LATEST_SORTING;
            }
        }
    }

}
