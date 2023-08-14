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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static com.example.waggle.exception.ErrorCode.REFRESH_TOKEN_NOT_FOUND;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.data.repository.util.ClassUtils.hasProperty;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Slf4j
//@Import(GlobalModelExceptionHandler.class)
class StoryControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void errorTestController() throws Exception {
        // Mocking the behavior of storyService.saveStory(storyDto) to throw CustomException
        //when(storyService.saveStory(any(StoryWriteDto.class))).thenThrow(new CustomException(REFRESH_TOKEN_NOT_FOUND));

//        mvc.perform(MockMvcRequestBuilders.post("/write").param("title", "story"))
//                .andExpect(status().is4xxClientError()) // HTTP 4xx Client Error is expected due to exception
//                .andExpect(view().name("error")) // Expected error view name
//                .andExpect(model().attributeExists("errorMessage")) // Model attribute should exist
//                .andExpect(model().attribute("errorMessage", Matchers.instanceOf(ErrorDto.class))); // Verify the attribute type

    }
}

