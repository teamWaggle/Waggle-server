package com.example.waggle.domain.board.story.service;

import com.example.waggle.web.dto.story.StoryDetailDto;
import com.example.waggle.web.dto.story.StorySummaryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StoryQueryService {

    List<StorySummaryDto> getStories();

    Page<StorySummaryDto> getPagedStoriesByUsername(String username, Pageable pageable);


    Page<StorySummaryDto> getPagedStories(Pageable pageable);

    StoryDetailDto getStoryByBoardId(Long boardId);
}
