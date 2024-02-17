package com.example.waggle.domain.board.service;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.hashtag.entity.Hashtag;
import com.example.waggle.domain.member.entity.Member;

public interface BoardService {

    Board getBoard(Long boardId, BoardType boardType);

    void saveHashtag(Board board, String tag);

    Hashtag getHashtag(String tag);

    boolean validateMemberUseBoard(Long boardId, BoardType boardType);

    boolean validateMemberUseBoard(Long boardId, BoardType boardType, Member member);


}
