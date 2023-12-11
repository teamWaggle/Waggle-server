package com.example.waggle.domain.board.story.service;

import com.example.waggle.web.dto.story.StoryRequest;

import java.io.IOException;

public interface StoryCommandService {
    Long createStory(StoryRequest.Post request) throws IOException;

    Long updateStory(Long boardId, StoryRequest.Post storyWriteDto) throws IOException;

    void deleteStory(Long boardId);
}
