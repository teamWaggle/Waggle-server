package com.example.waggle.controller;

import com.example.waggle.dto.ErrorDto;
import com.example.waggle.dto.board.story.StoryWriteDto;
import com.example.waggle.exception.CustomException;
import com.example.waggle.exception.GlobalModelExceptionHandler;
import com.example.waggle.service.board.StoryService;
import com.example.waggle.service.member.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.example.waggle.exception.ErrorCode.REFRESH_TOKEN_NOT_FOUND;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.servlet.function.RequestPredicates.contentType;

//@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.MOCK)
//@AutoConfigureMockMvc
@Slf4j
//@Import(GlobalModelExceptionHandler.class)
@WebAppConfiguration
class StoryControllerTest {

    MockMvc mvc;
    @InjectMocks
    StoryController storyController;
    @Mock
    MemberService memberService;
    @Mock
    StoryService storyService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void errorTestController() throws Exception {
        // Mocking the behavior of storyService.saveStory(storyDto) to throw CustomException
//        StoryWriteDto testStory = StoryWriteDto.builder().content("test story").build();
//        when(storyController.singleStoryWrite(testStory)).thenThrow(new CustomException(REFRESH_TOKEN_NOT_FOUND));
//
//
//        mvc.perform(MockMvcRequestBuilders.post("/write")
//                        .content(objectMapper.writeValueAsString(testStory))
//                        .contentType(APPLICATION_JSON_UTF8))
//                .andExpect(status().is4xxClientError()) // HTTP 4xx Client Error is expected due to exception
//                .andExpect(view().name("error")) // Expected error view name
//                .andExpect(model().attributeExists("errorMessage")) // Model attribute should exist
//                .andExpect(model().attribute("errorMessage", Matchers.instanceOf(ErrorDto.class))); // Verify the attribute type

    }
}

