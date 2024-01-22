package com.example.waggle.domain.board.story.service;

import com.example.waggle.web.dto.media.MediaRequest;
import com.example.waggle.web.dto.story.StoryRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface StoryCommandService {

    Long createStory(StoryRequest.Post request, List<MultipartFile> multipartFiles) throws IOException;

    Long createStoryByUsername(StoryRequest.Post request, List<MultipartFile> multipartFiles, String username) throws IOException;

    Long updateStory(Long boardId,
                     StoryRequest.Post storyWriteDto,
                     List<MultipartFile> multipartFiles,
                     List<String> deleteFiles) throws IOException;

    Long updateStoryV2(Long boardId,
                       StoryRequest.Post storyWriteDto,
                       MediaRequest.Put mediaListDto,
                       List<MultipartFile> multipartFiles) throws IOException;

    void deleteStory(Long boardId);
}
