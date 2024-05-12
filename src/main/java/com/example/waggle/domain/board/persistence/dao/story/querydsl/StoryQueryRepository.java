package com.example.waggle.domain.board.persistence.dao.story.querydsl;

import com.example.waggle.domain.board.persistence.entity.Story;
import com.example.waggle.domain.board.presentation.dto.story.StorySortParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoryQueryRepository {
    Page<Story> findStoriesBySortParam(StorySortParam sortParam, Pageable pageable);
}
