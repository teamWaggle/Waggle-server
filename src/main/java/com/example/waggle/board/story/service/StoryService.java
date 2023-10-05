package com.example.waggle.board.story.service;

import com.example.waggle.board.story.dto.StorySummaryDto;
import com.example.waggle.board.story.dto.StoryDetailDto;
import com.example.waggle.board.story.dto.StoryWriteDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StoryService {
    List<StorySummaryDto> getStories();

    List<StorySummaryDto> getStoriesByUsername(String username);

    Page<StorySummaryDto> getStoriesBySortingDateDesc(Pageable pageable);

    Page<StorySummaryDto> getStoriesBySortingRecommendDesc(Pageable pageable);

    Page<StorySummaryDto> getStoriesPaging(Pageable pageable);

    StoryDetailDto getStoryByBoardId(Long boardId);

    Long createStory(StoryWriteDto storyWriteDto);

    Long saveStoryWithThumbnail(StoryWriteDto storyWriteDto, String thumbnail);

    Long updateStory(Long storyId, StoryWriteDto storyWriteDto);

    StoryWriteDto getStoryWriteDtoByBoardId(Long boardId);

    boolean validateMember(Long boardId);

    void deleteStory(Long boardId);
}
