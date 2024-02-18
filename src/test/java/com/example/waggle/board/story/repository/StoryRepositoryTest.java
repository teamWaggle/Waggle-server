package com.example.waggle.board.story.repository;

import com.example.waggle.domain.board.story.entity.Story;
import com.example.waggle.domain.board.story.repository.StoryRepository;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
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
    @Autowired
    MemberRepository memberRepository;


    @Test
    @Transactional
    void 생성_내림차순_정렬() {

        Member member = Member.builder().email("34567").nickname("234289").userUrl("hi").username("238387384").password("78y93284").build();
        memberRepository.save(member);

        Story test1 = Story.builder().content("dkdkdk").member(member).build();
        Story test2 = Story.builder().content("33333").member(member).build();
        Story test3 = Story.builder().content("dlwjdgks").member(member).build();
        storyRepository.save(test1);
        storyRepository.save(test2);
        storyRepository.save(test3);

        List<Story> allByOrderByCreatedDateDesc = storyRepository.findAllByOrderByCreatedDateDesc();
        for (Story story : allByOrderByCreatedDateDesc) {
            log.info("story content = {}", story.getContent());
            log.info("story createDate = {}", story.getCreatedDate());
        }
    }

    @Test
    @Transactional
    void 페이징_내림차순_정렬() {
        Member member = Member.builder().email("34567").nickname("234289").username("238387384").userUrl("hi").password("78y93284").build();
        memberRepository.save(member);

        for (int i = 0; i < 20; i++) {
            String content = Integer.toString(i);
            Story story = Story.builder().content(content).member(member).build();
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