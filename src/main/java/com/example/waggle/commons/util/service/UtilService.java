package com.example.waggle.commons.util.service;

import com.example.waggle.board.Board;
import com.example.waggle.hashtag.domain.Hashtag;
import com.example.waggle.member.domain.Member;

public interface UtilService {

    Member getMember(String username);
    boolean login();
    Member getSignInMember();
    Board getBoard(Long boardId, BoardType boardType);
    void saveHashtag(Board board, String tag);
    Hashtag getHashtag(String tag);

    boolean validateMemberUseBoard(Long boardId, BoardType boardType);

}
