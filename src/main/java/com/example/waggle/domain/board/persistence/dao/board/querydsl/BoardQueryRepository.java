package com.example.waggle.domain.board.persistence.dao.board.querydsl;

import com.example.waggle.domain.board.persistence.entity.BoardType;

public interface BoardQueryRepository {
    void deleteBoardsWithRelations(BoardType boardType, Long boardId);
}
