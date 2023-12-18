package com.example.waggle.domain.hashtag.repository;

import com.example.waggle.domain.hashtag.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;



public interface HashtagRepository extends JpaRepository<Hashtag,Long> {

    Optional<Hashtag> findByContent(String content);

}
