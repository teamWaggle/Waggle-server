package com.example.waggle.service.board;

import com.example.waggle.dto.board.StoryDto;
import com.example.waggle.dto.board.StorySimpleDto;
import com.example.waggle.service.member.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StoryServiceTest {

    @Autowired
    StoryService storyService;
    @Autowired
    MemberService memberService;

    //read test

    @Test
    public void findStories() {
        List<StorySimpleDto> allStory = storyService.findAllStory();
        assertThat(allStory.stream().findFirst().get().getHashtags().size()).isEqualTo(1);
    }

    @Test
    public void findMemberStories() {
        List<StorySimpleDto> allStoryByMember = storyService.findAllStoryByMember("hann123");
        assertThat(allStoryByMember.size()).isEqualTo(2);
    }

    @Test
    public void findStory() {
        StoryDto storyByBoardId = storyService.findStoryByBoardId(1L);
        assertThat(storyByBoardId.getHashtags().size()).isEqualTo(1);
    }
}