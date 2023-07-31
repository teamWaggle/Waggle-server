package com.example.waggle.service.board;

import com.example.waggle.annotation.withMockUser.WithMockCustomUser;
import com.example.waggle.api.ApiResponse;
import com.example.waggle.component.jwt.JwtToken;
import com.example.waggle.component.jwt.SecurityUtil;
import com.example.waggle.dto.board.story.StoryViewDto;
import com.example.waggle.dto.board.story.StorySimpleViewDto;
import com.example.waggle.dto.board.story.StoryWriteDto;
import com.example.waggle.dto.member.SignInDto;
import com.example.waggle.dto.member.SignUpDto;
import com.example.waggle.service.member.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
class StoryServiceTest {

    @Autowired
    private StoryService storyService;
    @Autowired
    private MemberService memberService;

    SignUpDto signUpDto1;
    SignUpDto signUpDto2;
    SignInDto signInDto;

    StoryWriteDto storyWriteDto1;
    StoryWriteDto storyWriteDto2;

    List<String> tags1 = new ArrayList<>();
    List<String> tags2 = new ArrayList<>();


    @BeforeEach
    void setDto() {
        tags1.add("choco");
        tags1.add("poodle");
        tags2.add("poodle");

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

        signInDto = SignInDto.builder()
                .username("member1")
                .password("12345678")
                .build();

        storyWriteDto1 = StoryWriteDto.builder()
                .content("i love my choco")
                .username("member1")
                .hashtags(tags1)
                .thumbnail("www.waggle")
                .build();
        storyWriteDto2 = StoryWriteDto.builder()
                .content("how can i do make he is happy?")
                .username("member2")
                .hashtags(tags2)
                .thumbnail("www.waggle")
                .build();
    }

    private void setBoardAndMember() {
        //member set
        memberService.signUp(signUpDto1);
        memberService.signUp(signUpDto2);

        //story set
        storyService.saveStory(storyWriteDto1);
        storyService.saveStory(storyWriteDto2);
    }


    @Test
    @Transactional
    @WithMockCustomUser
    void findAll() throws Exception{
        //given
        setBoardAndMember();

        //when
        List<StorySimpleViewDto> allStory = storyService.findAllStory();

        //then
        assertThat(allStory.size()).isEqualTo(2);
        assertThat(allStory.get(0).getUsername()).isEqualTo("member1");

        log.info("success");
    }

    @Test
    @Transactional
    @WithMockCustomUser
    void findAllStoryByMember() {
        //given
        setBoardAndMember();

        //when
        List<StorySimpleViewDto> allStoryByMember = storyService.findAllStoryByMember();

        //then
        assertThat(allStoryByMember.size()).isEqualTo(2);
    }

    @Test
    @Transactional
    @WithMockCustomUser
    void findStoryViewByBoardId() {
        //given
        setBoardAndMember();

        //when
        StoryViewDto storyViewByBoardId = storyService.findStoryViewByBoardId(1L);

        //then
        assertThat(storyViewByBoardId.getUsername()).isEqualTo("member1");
    }

    @Test
    @Transactional
    @WithMockCustomUser
    void

}