package com.example.waggle.repository.board;

import com.example.waggle.domain.board.hashtag.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;



public interface HashtagRepository extends JpaRepository<Hashtag,Long> {

    Optional<Hashtag> findByTag(String content);

}
