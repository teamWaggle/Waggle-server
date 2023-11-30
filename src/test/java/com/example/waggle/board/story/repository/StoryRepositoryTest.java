package com.example.waggle.board.story.repository;

import com.example.waggle.domain.board.story.domain.Story;
import com.example.waggle.domain.board.story.repository.StoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Test
    @Transactional
    void 페이징_내림차순_정렬() {
        for (int i = 0; i < 20; i++) {
            String content = Integer.toString(i);
            Story story = Story.builder().content(content).build();
            storyRepository.save(story);
        }
        Sort createdDate = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(0, 10, createdDate);

        Page<Story> all = storyRepository.findAll(pageable);
        System.out.println("all.getTotalElements() = " + all.getTotalElements());
        for (Story story : all) {
            System.out.println("story.getContent() = " + story.getContent());
        }
    }
}