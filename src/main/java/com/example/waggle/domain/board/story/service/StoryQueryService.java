package com.example.waggle.domain.board.story.service;

import com.example.waggle.domain.board.story.entity.Story;
import com.example.waggle.web.dto.story.StoryFilterParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StoryQueryService {

    List<Story> getStories();

    Page<Story> getPagedStoriesByUsername(String username, Pageable pageable);

    Page<Story> getPagedStoriesByUserUrl(String userUrl, Pageable pageable);

    Page<Story> getPagedStoriesByMemberId(Long memberId, Pageable pageable);

    Page<Story> getPagedStories(Pageable pageable);

    Page<Story> getPagedStoriesByFilter(StoryFilterParam filterParam, Pageable pageable);

    Story getStoryByBoardId(Long boardId);


}
