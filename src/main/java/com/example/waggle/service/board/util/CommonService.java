package com.example.waggle.service.board.util;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.member.Member;

public interface CommonService {

    Member getMember(String username);
    boolean login();
    Member getSignInMember();
    Board getBoard(Long boardId, String boardType);

}
