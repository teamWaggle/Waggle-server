package com.example.waggle.domain.board.story.service;

import com.example.waggle.domain.board.story.entity.Story;
import com.example.waggle.domain.board.story.repository.StoryRepository;
import com.example.waggle.global.exception.handler.StoryHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class StoryQueryServiceImpl implements StoryQueryService {

    private final StoryRepository storyRepository;

    @Override
    public List<Story> getStories() {
        return storyRepository.findAll();
    }

    @Override
    public Page<Story> getPagedStoriesByUsername(String username, Pageable pageable) {
        return storyRepository.findByMemberUsername(username, pageable);
    }

    @Override
    public Page<Story> getPagedStoriesByUserUrl(String userUrl, Pageable pageable) {
        return storyRepository.findByMemberUsername(userUrl, pageable);
    }

    @Override
    public Page<Story> getPagedStoriesByMemberId(Long memberId, Pageable pageable) {
        return storyRepository.findByMemberId(memberId, pageable);
    }


    @Override
    public Page<Story> getPagedStories(Pageable pageable) {
        return storyRepository.findAll(pageable);
    }


    @Override
    public Story getStoryByBoardId(Long boardId) {
        return storyRepository.findById(boardId)
                .orElseThrow(() -> new StoryHandler(ErrorStatus.BOARD_NOT_FOUND));
    }
}
