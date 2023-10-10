package com.example.waggle.board.story.repository;

import com.example.waggle.board.story.domain.Story;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cglib.core.Local;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class StoryRepositoryTest {

    @Autowired
    StoryRepository storyRepository;


    @Test
    @Transactional
    void 생성_내림차순_정렬() {

        Story test1 = Story.builder().content("dkdkdk").build();
        Story test2 = Story.builder().content("33333").build();
        Story test3 = Story.builder().content("dlwjdgks").build();
        storyRepository.save(test1);
        storyRepository.save(test2);
        storyRepository.save(test3);

        List<Story> allByOrderByCreatedDateDesc = storyRepository.findAllByOrderByCreatedDateDesc();
        for (Story story : allByOrderByCreatedDateDesc) {
            log.info("story content = {}",story.getContent());
            log.info("story createDate = {}", story.getCreatedDate());
        }
    }

}