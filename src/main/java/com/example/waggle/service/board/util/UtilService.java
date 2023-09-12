package com.example.waggle.service.board.util;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.board.hashtag.Hashtag;
import com.example.waggle.domain.member.Member;

public interface UtilService {

    Member getMember(String username);
    boolean login();
    Member getSignInMember();
    Board getBoard(Long boardId, BoardType boardType);
    void saveHashtag(Board board, String tag);
    Hashtag getHashtag(String tag);

}
