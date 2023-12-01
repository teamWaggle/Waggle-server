package com.example.waggle.global.util.service;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.hashtag.entity.Hashtag;
import com.example.waggle.domain.member.entity.Member;

public interface UtilService {

    Member getMember(String username);
    boolean login();
    Member getSignInMember();
    Board getBoard(Long boardId, BoardType boardType);
    void saveHashtag(Board board, String tag);
    Hashtag getHashtag(String tag);

    boolean validateMemberUseBoard(Long boardId, BoardType boardType);

}
