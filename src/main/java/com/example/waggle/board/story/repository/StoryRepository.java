package com.example.waggle.board.story.repository;

import com.example.waggle.board.story.domain.Story;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface StoryRepository extends JpaRepository<Story, Long> {

    List<Story> findByMemberUsername(String username);

    List<Story> findAllByOrderByCreatedDateDesc();      //최신순

    Page<Story> findAll(Pageable pageable);


}