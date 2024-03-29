package com.example.waggle.domain.board.story.repository;

import com.example.waggle.domain.board.story.entity.Story;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface StoryRepository extends JpaRepository<Story, Long> {

    List<Story> findListByMemberUsername(String username);

    List<Story> findAllByOrderByCreatedDateDesc();      //최신순

    Page<Story> findAll(Pageable pageable);

    Page<Story> findByMemberUsername(String username, Pageable pageable);

    Page<Story> findByMemberId(Long memberId, Pageable pageable);

    void deleteAllByMemberUsername(String username);


}