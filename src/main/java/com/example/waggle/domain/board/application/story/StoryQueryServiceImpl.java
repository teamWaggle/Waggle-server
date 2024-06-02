package com.example.waggle.domain.board.application.story;

import com.example.waggle.domain.board.persistence.dao.story.jpa.StoryRepository;
import com.example.waggle.domain.board.persistence.entity.Story;
import com.example.waggle.domain.board.presentation.dto.story.StorySortParam;
import com.example.waggle.exception.object.handler.StoryHandler;
import com.example.waggle.exception.payload.code.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class StoryQueryServiceImpl implements StoryQueryService {

    private final StoryRepository storyRepository;

    @Override
    public Page<Story> getPagedStoryListByUserUrl(String userUrl, Pageable pageable) {
        return storyRepository.findByMemberUserUrl(userUrl, pageable);
    }


    @Override
    public Story getStoryByBoardId(Long boardId) {
        return storyRepository.findById(boardId)
                .orElseThrow(() -> new StoryHandler(ErrorStatus.BOARD_NOT_FOUND));
    }

    @Override
    public Page<Story> getPagedStoryListByKeywordAndSortParam(String keyword, StorySortParam sortParam, Pageable pageable) {
        return storyRepository.findStoryListByKeywordAndSortParam(keyword, sortParam, pageable);
    }
}
