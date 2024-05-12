package com.example.waggle.domain.board.application.story;

import com.example.waggle.domain.board.persistence.entity.Story;
import com.example.waggle.domain.board.presentation.dto.story.StorySortParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StoryQueryService {

    List<Story> getStories();

    Page<Story> getPagedStoriesByUsername(String username, Pageable pageable);

    Page<Story> getPagedStoriesByUserUrl(String userUrl, Pageable pageable);

    Page<Story> getPagedStoriesByMemberId(Long memberId, Pageable pageable);

    Page<Story> getPagedStories(Pageable pageable);

    Page<Story> getPagedStoriesBySortParam(StorySortParam sortParam, Pageable pageable);

    Story getStoryByBoardId(Long boardId);


}
