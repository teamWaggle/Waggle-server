package com.example.waggle.domain.board.story.service;

import com.example.waggle.web.dto.story.StoryRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface StoryCommandService {
    Long createStory(StoryRequest.Post request,
                     List<MultipartFile> multipartFiles,
                     MultipartFile thumbnail) throws IOException;

    Long updateStory(Long boardId, StoryRequest.Post storyWriteDto,
                     List<MultipartFile> multipartFiles,
                     MultipartFile thumbnail) throws IOException;

    boolean validateMember(Long boardId);

    void deleteStory(Long boardId);
}
