package com.example.waggle.domain.hashtag.repository;

import com.example.waggle.domain.hashtag.entity.BoardHashtag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardHashtagRepository extends JpaRepository<BoardHashtag, Long> {
}
