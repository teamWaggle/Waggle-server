package com.example.waggle.domain.board.application.story;

import com.example.waggle.domain.board.persistence.entity.Story;
import com.example.waggle.domain.board.presentation.dto.story.StorySortParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StoryQueryService {

    List<Story> getStories();

    Page<Story> getPagedStoryListByUsername(String username, Pageable pageable);

    Page<Story> getPagedStoryListByUserUrl(String userUrl, Pageable pageable);

    Page<Story> getPagedStoryListByMemberId(Long memberId, Pageable pageable);

    Page<Story> getPagedStoryList(Pageable pageable);

    Page<Story> getPagedStoryListBySortParam(StorySortParam sortParam, Pageable pageable);

    Story getStoryByBoardId(Long boardId);

    Page<Story> getPagedStoryListByKeyword(String keyword, Pageable pageable);

    Page<Story> getPagedStoryListByKeywordAndSortParam(String keyword, StorySortParam sortParam, Pageable pageable);


}
