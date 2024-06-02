package com.example.waggle.domain.board.application.story;

import com.example.waggle.domain.board.persistence.entity.Story;
import com.example.waggle.domain.board.presentation.dto.story.StorySortParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoryQueryService {

    Page<Story> getPagedStoryListByUserUrl(String userUrl, Pageable pageable);

    Story getStoryByBoardId(Long boardId);

    Page<Story> getPagedStoryListByKeywordAndSortParam(String keyword, StorySortParam sortParam, Pageable pageable);


}
