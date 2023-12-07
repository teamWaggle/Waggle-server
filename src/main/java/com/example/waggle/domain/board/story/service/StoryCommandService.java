package com.example.waggle.domain.board.story.service;

import com.example.waggle.web.dto.story.StoryRequest;

import java.io.IOException;
import java.util.List;

public interface StoryCommandService {
    Long createStory(StoryRequest.Post request,
                     List<String> multipartFiles,
                     String thumbnail) throws IOException;

    Long updateStory(Long boardId, StoryRequest.Post storyWriteDto,
                     List<String> multipartFiles,
                     String thumbnail) throws IOException;

    void deleteStory(Long boardId);
}
