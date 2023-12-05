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
public class StoryQueryServiceImpl implements StoryQueryService{

    private final StoryRepository storyRepository;

    @Override
    public List<Story> getStories() {
        List<Story> stories = storyRepository.findAll();
        return stories;

    }

    @Override
    public Page<Story> getPagedStoriesByUsername(String username, Pageable pageable) {
        Page<Story> stories = storyRepository.findByMemberUsername(username,pageable);
        return stories;
    }


    @Override
    public Page<Story> getPagedStories(Pageable pageable) {
        Page<Story> all = storyRepository.findAll(pageable);
        return all;
    }


    @Override
    public Story getStoryByBoardId(Long boardId) {
        Story story = storyRepository.findById(boardId)
                .orElseThrow(() -> new StoryHandler(ErrorStatus.BOARD_NOT_FOUND));
        return story;
    }
}
