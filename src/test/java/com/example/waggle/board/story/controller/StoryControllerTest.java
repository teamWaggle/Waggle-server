package com.example.waggle.board.story.controller;

import com.example.waggle.web.controller.StoryApiController;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

//@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.MOCK)
//@AutoConfigureMockMvc
@Slf4j
//@Import(GlobalModelExceptionHandler.class)
@WebAppConfiguration
class StoryControllerTest {

    MockMvc mvc;
    @InjectMocks
    StoryApiController storyController;

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

