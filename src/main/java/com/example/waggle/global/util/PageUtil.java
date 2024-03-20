package com.example.waggle.global.util;

import com.example.waggle.domain.board.Board;
import org.springframework.data.domain.Page;

public class PageUtil {
    public static int THIS_PAGE_IS_LAST = -1;
    public static int STORY_SIZE = 9;

    public static <T extends Board> int countNextPage(Page<T> pagedBoardList) {
        if (pagedBoardList.isLast()) return THIS_PAGE_IS_LAST;
        return pagedBoardList.getNumber() + 1;
    }
}
