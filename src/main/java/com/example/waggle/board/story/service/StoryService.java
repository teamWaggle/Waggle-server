package com.example.waggle.board.story.service;

import com.example.waggle.board.story.dto.StorySimpleViewDto;
import com.example.waggle.board.story.dto.StoryViewDto;
import com.example.waggle.board.story.dto.StoryWriteDto;

import java.util.List;

public interface StoryService {
    public List<StorySimpleViewDto> findAllStory();

    public List<StorySimpleViewDto> findAllStoryByUsername(String username);

    public StoryViewDto findStoryViewByBoardId(Long id);

    public Long saveStory(StoryWriteDto saveStoryDto);

    public Long saveStoryWithThumbnail(StoryWriteDto saveStoryDto, String thumbnail);

    public String changeStory(StoryWriteDto storyDto);

    public StoryWriteDto findStoryWriteByBoardId(Long id);

    public boolean checkMember(Long boardId);

    public void removeStory(StoryViewDto storyDto);
}
