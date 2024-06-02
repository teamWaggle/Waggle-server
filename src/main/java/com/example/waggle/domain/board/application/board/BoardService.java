package com.example.waggle.domain.board.application.board;

import com.example.waggle.domain.board.persistence.entity.Board;
import com.example.waggle.domain.board.persistence.entity.BoardType;
import com.example.waggle.domain.hashtag.persistence.entity.Hashtag;
import com.example.waggle.domain.member.persistence.entity.Member;

public interface BoardService {

    void saveHashtag(Board board, String tag);

    Hashtag getHashtag(String tag);

    boolean validateMemberUseBoard(Long boardId, BoardType boardType, Member member);


}
