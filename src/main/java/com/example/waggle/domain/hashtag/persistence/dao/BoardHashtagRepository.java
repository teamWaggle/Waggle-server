package com.example.waggle.domain.hashtag.persistence.dao;

import com.example.waggle.domain.board.persistence.entity.Board;
import com.example.waggle.domain.hashtag.persistence.entity.BoardHashtag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardHashtagRepository extends JpaRepository<BoardHashtag, Long> {

    void deleteAllByBoard(Board board);

}
