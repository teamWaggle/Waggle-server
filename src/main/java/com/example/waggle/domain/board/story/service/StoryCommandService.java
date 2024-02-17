package com.example.waggle.domain.board.story.service;

import com.example.waggle.web.dto.media.MediaRequest;
import com.example.waggle.web.dto.story.StoryRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StoryCommandService {

    Long createStory(StoryRequest.Post request, List<MultipartFile> multipartFiles);

    Long createStoryByUsername(StoryRequest.Post request, List<MultipartFile> multipartFiles, String username);

    Long updateStory(Long boardId,
                     StoryRequest.Post storyWriteDto,
                     List<MultipartFile> multipartFiles,
                     List<String> deleteFiles);

    Long updateStoryV2(Long boardId,
                       StoryRequest.Post storyWriteDto,
                       MediaRequest.Put mediaListDto,
                       List<MultipartFile> multipartFiles);

    Long updateStoryByUsername(Long boardId,
                               String username,
                               StoryRequest.Post storyWriteDto,
                               MediaRequest.Put mediaListDto,
                               List<MultipartFile> multipartFiles);

    void deleteStory(Long boardId);

    void deleteStoryByUsername(Long boardId, String username);
}
