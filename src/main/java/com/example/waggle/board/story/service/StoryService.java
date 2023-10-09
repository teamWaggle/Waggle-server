package com.example.waggle.board.story.service;

import com.example.waggle.board.story.dto.StorySummaryDto;
import com.example.waggle.board.story.dto.StoryDetailDto;
import com.example.waggle.board.story.dto.StoryWriteDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface StoryService {
    List<StorySummaryDto> getStories();

    List<StorySummaryDto> getStoriesByUsername(String username);

    StoryDetailDto getStoryByBoardId(Long boardId);

    Long createStory(StoryWriteDto storyWriteDto, List<MultipartFile> multipartFiles, MultipartFile thumbnail) throws IOException;

    Long updateStory(Long boardId, StoryWriteDto storyWriteDto, List<MultipartFile> multipartFiles, MultipartFile thumbnail) throws IOException;

    StoryWriteDto getStoryWriteDtoByBoardId(Long boardId);

    boolean validateMember(Long boardId);

    void deleteStory(Long boardId);
}
