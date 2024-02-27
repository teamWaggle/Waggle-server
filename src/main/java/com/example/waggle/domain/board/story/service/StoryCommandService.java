package com.example.waggle.domain.board.story.service;

import com.example.waggle.web.dto.media.MediaRequest.MediaUpdateDto;
import com.example.waggle.web.dto.story.StoryRequest;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface StoryCommandService {

    Long createStory(StoryRequest createStoryRequest, List<MultipartFile> multipartFiles);

    Long createStoryByUsername(StoryRequest createStoryRequest, List<MultipartFile> multipartFiles, String username);

    Long updateStory(Long boardId,
                     StoryRequest updateStoryRequest,
                     List<MultipartFile> multipartFiles,
                     List<String> deleteFiles);

    Long updateStoryV2(Long boardId,
                       StoryRequest updateStoryRequest,
                       MediaUpdateDto updateMediaRequest,
                       List<MultipartFile> multipartFiles);

    Long updateStoryByUsername(Long boardId,
                               String username,
                               StoryRequest createStoryRequest,
                               MediaUpdateDto updateMediaRequest,
                               List<MultipartFile> multipartFiles);

    void deleteStory(Long boardId);

    void deleteStoryByUsername(Long boardId, String username);
}
