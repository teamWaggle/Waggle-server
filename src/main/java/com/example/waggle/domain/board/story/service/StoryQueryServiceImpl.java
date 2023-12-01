package com.example.waggle.domain.board.story.service;

import com.example.waggle.domain.board.story.entity.Story;
import com.example.waggle.domain.board.story.repository.StoryRepository;
import com.example.waggle.global.exception.CustomPageException;
import com.example.waggle.web.dto.story.StoryDetailDto;
import com.example.waggle.web.dto.story.StorySummaryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.waggle.global.exception.ErrorCode.BOARD_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class StoryQueryServiceImpl implements StoryQueryService{

    private final StoryRepository storyRepository;

    @Override
    public List<StorySummaryDto> getStories() {
        List<Story> stories = storyRepository.findAll();
        return stories.stream().map(StorySummaryDto::toDto).collect(Collectors.toList());

    }

    @Override
    public Page<StorySummaryDto> getPagedStoriesByUsername(String username, Pageable pageable) {
        Page<Story> stories = storyRepository.findByMemberUsername(username,pageable);
        return stories.map(StorySummaryDto::toDto);
    }


    @Override
    public Page<StorySummaryDto> getPagedStories(Pageable pageable) {
        Page<Story> all = storyRepository.findAll(pageable);
        return all.map(StorySummaryDto::toDto);
    }


    @Override
    public StoryDetailDto getStoryByBoardId(Long boardId) {
        Story story = storyRepository.findById(boardId)
                .orElseThrow(() -> new CustomPageException(BOARD_NOT_FOUND));
        return StoryDetailDto.toDto(story);
    }
}
