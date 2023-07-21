package com.example.waggle.repository.board.boardtype;

import com.example.waggle.domain.board.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import java.util.Optional;


public interface StoryRepository extends JpaRepository<Story, Long> {

    List<Story> findByHashtagsTag(String tag);
    List<Story> findByMemberUsername(String username);

}