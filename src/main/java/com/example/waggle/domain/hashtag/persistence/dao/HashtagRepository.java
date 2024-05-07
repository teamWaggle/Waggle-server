package com.example.waggle.domain.hashtag.persistence.dao;

import com.example.waggle.domain.hashtag.persistence.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

    Optional<Hashtag> findByContent(String content);

}
