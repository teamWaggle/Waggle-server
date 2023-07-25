package com.example.waggle.service.board;

import com.example.waggle.dto.board.story.StoryViewDto;
import com.example.waggle.dto.board.story.StorySimpleViewDto;
import com.example.waggle.service.member.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class StoryServiceTest {

    @Autowired
    StoryService storyService;
    @Autowired
    MemberService memberService;

    //read test

<<<<<<< HEAD
=======
    @Test
    public void findStories() {
        List<StorySimpleViewDto> allStory = storyService.findAllStory();
        assertThat(allStory.stream().findFirst().get().getHashtags().size()).isEqualTo(1);
    }

    @Test
    public void findMemberStories() {
        List<StorySimpleViewDto> allStoryByMember = storyService.findAllStoryByMember("hann123");
        assertThat(allStoryByMember.size()).isEqualTo(2);
    }

    @Test
    public void findStory() {
        StoryViewDto storyByBoardId = storyService.findStoryByBoardId(1L);
        System.out.println("storyByBoardId.getContent() = " + storyByBoardId.getContent());
    }

    //create test
    @Test
    public void createStory() {
        System.out.println("============start=============");
        List<String> hashtags = new ArrayList<>();
        hashtags.add("choco");
        hashtags.add("poodle");
        List<String> medias = new ArrayList<>();
        medias.add("choco.img");
        StoryViewDto hann123 = StoryViewDto.builder()
                .content("hello~")
                .username("hann123")
                .hashtags(hashtags)
                .medias(medias)
                .thumbnail("choco.img").build();
        storyService.saveStory(hann123);

        List<StorySimpleViewDto> allStory = storyService.findAllStory();
        for (StorySimpleViewDto storySimpleDto : allStory) {
            for (String hashtag : storySimpleDto.getHashtags()) {
                System.out.println("hashtag = " + hashtag);
            }
        }
    }

    //update test
    @Test
    public void changeStory() {
        System.out.println("============start=============");
        List<String> hashtags = new ArrayList<>();
        hashtags.add("choco");
        hashtags.add("poodle");
        List<String> medias = new ArrayList<>();
        medias.add("choco.img");
        StoryViewDto hann123 = StoryViewDto.builder()
                .content("hello~")
                .username("hann123")
                .id(1L)
                .hashtags(hashtags)
                .medias(medias)
                .thumbnail("choco.img").build();
//        storyService.changeStory(hann123);

        StoryViewDto storyByBoardId = storyService.findStoryByBoardId(1L);
        System.out.println("storyByBoardId.getContent() = " + storyByBoardId.getContent());
    }
>>>>>>> develop
    
}