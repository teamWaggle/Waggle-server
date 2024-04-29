package com.example.waggle.domain.board.story.repository;

import com.example.waggle.domain.board.story.entity.Story;
import com.example.waggle.web.dto.story.StoryFilterParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoryQueryRepository {
    Page<Story> findStoriesByFilter(StoryFilterParam filterParam, Pageable pageable);
}
