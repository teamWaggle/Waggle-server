package com.example.waggle.service.board;

import com.example.waggle.annotation.withMockUser.WithMockCustomUser;
import com.example.waggle.component.DatabaseCleanUp;
import com.example.waggle.domain.board.boardType.Story;
import com.example.waggle.domain.member.Member;
import com.example.waggle.dto.board.story.StorySimpleViewDto;
import com.example.waggle.dto.board.story.StoryViewDto;
import com.example.waggle.dto.board.story.StoryWriteDto;
import com.example.waggle.dto.member.SignUpDto;
import com.example.waggle.repository.board.boardtype.StoryRepository;
import com.example.waggle.repository.member.MemberRepository;
import com.example.waggle.service.board.util.BoardType;
import com.example.waggle.service.member.MemberService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RecommendServiceTest {

    @Autowired
    private StoryService storyService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private RecommendService recommendService;
    @Autowired
    private StoryRepository storyRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    DatabaseCleanUp databaseCleanUp;


    SignUpDto signUpDto1;
    SignUpDto signUpDto2;

    StoryWriteDto storyWriteDto1;
    StoryWriteDto storyWriteDto2;

    List<String> tags1 = new ArrayList<>();
    List<String> tags2 = new ArrayList<>();
    List<String> medias1 = new ArrayList<>();
    List<String> medias2 = new ArrayList<>();


    @BeforeEach
    void setDto() {
        tags1.add("choco");
        tags1.add("poodle");
        tags2.add("poodle");

        medias1.add("media1");
        medias1.add("mediamedia1");
        medias2.add("media2");
        medias2.add("mediamedia2");

        signUpDto1 = SignUpDto.builder()
                .username("member1")
                .password("12345678")
                .nickname("닉네임1")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();

        signUpDto2 = SignUpDto.builder()
                .username("member2")
                .password("12345678")
                .nickname("닉네임2")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();

        storyWriteDto1 = StoryWriteDto.builder()
                .content("i love my choco")
                .hashtags(tags1)
                .medias(medias1)
                .thumbnail("www.waggle")
                .build();

        storyWriteDto2 = StoryWriteDto.builder()
                .content("how can i do make he is happy?")
                .hashtags(tags2)
                .medias(medias2)
                .thumbnail("www.waggle")
                .build();



    }
    @AfterEach
    void clean() {
        databaseCleanUp.truncateAllEntity();
    }

    @Transactional
    void setBoardAndMember() {

        //member set
        memberService.signUp(signUpDto1);
        memberService.signUp(signUpDto2);

        Member build = Member.builder().username("user1").password("password").build();
        memberRepository.save(build);

        Story iiii = Story.builder().member(build).content("iiii").build();
        storyRepository.save(iiii);

        //story set
        storyService.saveStory(storyWriteDto1);
        //storyService.saveStory(storyWriteDto2);
    }

    @Test
    @WithMockCustomUser
    void recommendBoard() {
        //given
        setBoardAndMember();
        StorySimpleViewDto storySimpleViewDto = storyService.findAllStory().get(0);

        //when
        recommendService.clickRecommend(storySimpleViewDto.getId(), BoardType.STORY);
        StoryViewDto storyViewByBoardId = storyService.findStoryViewByBoardId(storySimpleViewDto.getId());
        recommendService.checkRecommend(storyViewByBoardId);

        //then
        assertThat(storyViewByBoardId.getRecommendCount()).isEqualTo(1);
    }
    @Test
    @WithMockCustomUser
    void cancelRecommendBoard() {
        //given
        setBoardAndMember();
        StorySimpleViewDto storySimpleViewDto = storyService.findAllStory().get(0);
        recommendService.clickRecommend(storySimpleViewDto.getId(),BoardType.STORY);
        recommendService.clickRecommend(storySimpleViewDto.getId(),BoardType.STORY);

        //when
        StoryViewDto storyViewByBoardId = storyService.findStoryViewByBoardId(storySimpleViewDto.getId());
        recommendService.checkRecommend(storyViewByBoardId);

        //then
        assertThat(storyViewByBoardId.getRecommendCount()).isEqualTo(0);
    }
}