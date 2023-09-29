package com.example.waggle.repository.board.boardtype;

import com.example.waggle.domain.board.boardType.Story;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface StoryRepository extends JpaRepository<Story, Long> {

    List<Story> findByMemberUsername(String username);

}