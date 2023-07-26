package com.example.waggle.repository.board;

import com.example.waggle.domain.board.hashtag.BoardHashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardHashtagRepository extends JpaRepository<BoardHashtag, Long> {

    List<BoardHashtag> findByHashtagTag(String tag);
}
