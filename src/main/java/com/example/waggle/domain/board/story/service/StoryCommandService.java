package com.example.waggle.domain.board.story.service;

import com.example.waggle.web.dto.story.StoryWriteDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface StoryCommandService {
    Long createStory(StoryWriteDto storyWriteDto, List<MultipartFile> multipartFiles, MultipartFile thumbnail) throws IOException;

    Long updateStory(Long boardId, StoryWriteDto storyWriteDto, List<MultipartFile> multipartFiles, MultipartFile thumbnail) throws IOException;

    StoryWriteDto getStoryWriteDtoByBoardId(Long boardId);

    boolean validateMember(Long boardId);

    void deleteStory(Long boardId);
}
