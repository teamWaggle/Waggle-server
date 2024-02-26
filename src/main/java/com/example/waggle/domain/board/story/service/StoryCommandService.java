package com.example.waggle.domain.board.story.service;

import com.example.waggle.web.dto.media.MediaRequest.MediaUpdateDto;
import com.example.waggle.web.dto.story.StoryRequest.StoryCreateDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StoryCommandService {

    Long createStory(StoryCreateDto request, List<MultipartFile> multipartFiles);

    Long createStoryByUsername(StoryCreateDto request, List<MultipartFile> multipartFiles, String username);

    Long updateStory(Long boardId,
                     StoryCreateDto storyWriteDto,
                     List<MultipartFile> multipartFiles,
                     List<String> deleteFiles);

    Long updateStoryV2(Long boardId,
                       StoryCreateDto storyWriteDto,
                       MediaUpdateDto mediaListDto,
                       List<MultipartFile> multipartFiles);

    Long updateStoryByUsername(Long boardId,
                               String username,
                               StoryCreateDto storyWriteDto,
                               MediaUpdateDto mediaListDto,
                               List<MultipartFile> multipartFiles);

    void deleteStory(Long boardId);

    void deleteStoryByUsername(Long boardId, String username);
}
