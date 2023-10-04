package com.example.waggle.board.story.service;

import com.example.waggle.board.story.domain.Story;
import com.example.waggle.board.story.dto.StoryDetailDto;
import com.example.waggle.board.story.dto.StorySummaryDto;
import com.example.waggle.board.story.dto.StoryWriteDto;
import com.example.waggle.board.story.repository.StoryRepository;
import com.example.waggle.commons.exception.CustomPageException;
import com.example.waggle.commons.util.service.UtilService;
import com.example.waggle.media.domain.Media;
import com.example.waggle.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.waggle.commons.exception.ErrorCode.BOARD_NOT_FOUND;
import static com.example.waggle.commons.exception.ErrorCode.CANNOT_TOUCH_NOT_YOURS;


@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class StoryServiceImpl implements StoryService {

    private final StoryRepository storyRepository;
    private final UtilService utilService;


    @Override
    public List<StorySummaryDto> getStories() {
        List<Story> stories = storyRepository.findAll();
        return stories.stream().map(StorySummaryDto::toDto).collect(Collectors.toList());

    }

    @Override
    public List<StorySummaryDto> getStoriesByUsername(String username) {
        List<Story> stories = storyRepository.findByMemberUsername(username);
        return stories.stream().map(StorySummaryDto::toDto).collect(Collectors.toList());
    }

    @Override
    public List<StorySummaryDto> getStoriesBySortingDateDesc() {
        List<Story> storiesByDateDesc = storyRepository.findAllByOrderByCreatedDateDesc();
        return storiesByDateDesc.stream()
                .map(StorySummaryDto::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<StorySummaryDto> getStoriesBySortingRecommendDesc() {

        return null;
    }


    @Override
    public StoryDetailDto getStoryByBoardId(Long boardId) {
        Story story = storyRepository.findById(boardId)
                .orElseThrow(() -> new CustomPageException(BOARD_NOT_FOUND));
        return StoryDetailDto.toDto(story);
    }

    @Transactional
    @Override
    public Long createStory(StoryWriteDto storyWriteDto) {
        Member signInMember = utilService.getSignInMember();
        Story saveStory = storyWriteDto.toEntity(signInMember);
        storyRepository.save(saveStory);

        if (!storyWriteDto.getHashtags().isEmpty()) {
            for (String hashtagContent : storyWriteDto.getHashtags()) {
                utilService.saveHashtag(saveStory, hashtagContent);
            }
        }

        if (!storyWriteDto.getMedias().isEmpty()) {
            for (String mediaURL : storyWriteDto.getMedias()) {
                Media.builder().url(mediaURL).board(saveStory).build().linkBoard(saveStory);
            }
        }
        return saveStory.getId();
    }

    @Transactional
    @Override
    public Long saveStoryWithThumbnail(StoryWriteDto storyWriteDto, String thumbnail) {
        Member signInMember = utilService.getSignInMember();
        storyWriteDto.changeThumbnail(thumbnail);

        Story saveStory = storyWriteDto.toEntity(signInMember);
        storyRepository.save(saveStory);

        if (!storyWriteDto.getHashtags().isEmpty()) {
            for (String hashtagContent : storyWriteDto.getHashtags()) {
                utilService.saveHashtag(saveStory, hashtagContent);
            }
        }

        if (!storyWriteDto.getMedias().isEmpty()) {
            for (String mediaURL : storyWriteDto.getMedias()) {
                Media.builder().url(mediaURL).board(saveStory).build().linkBoard(saveStory);
            }
        }

        return saveStory.getId();
    }


    @Transactional
    @Override
    public Long updateStory(Long boardId, StoryWriteDto storyWriteDto) {
        Story story = storyRepository.findById(boardId)
                .orElseThrow(() -> new CustomPageException(BOARD_NOT_FOUND));


        story.changeStory(storyWriteDto.getContent(), storyWriteDto.getThumbnail());

        story.getMedias().clear();

        for (String media : storyWriteDto.getMedias()) {
            Media.builder().url(media).board(story).build().linkBoard(story);
        }

        story.getBoardHashtags().clear();

        for (String hashtag : storyWriteDto.getHashtags()) {
            utilService.saveHashtag(story, hashtag);
        }
        return story.getId();
    }


    @Override
    public StoryWriteDto getStoryWriteDtoByBoardId(Long boardId) {
        Story story = storyRepository.findById(boardId)
                .orElseThrow(() -> new CustomPageException(BOARD_NOT_FOUND));
        return StoryWriteDto.toDto(story);
    }

    @Override
    public boolean validateMember(Long boardId) {
        Member signInMember = utilService.getSignInMember();
        Story story = storyRepository.findById(boardId)
                .orElseThrow(() -> new CustomPageException(BOARD_NOT_FOUND));
        return story.getMember().equals(signInMember);
    }

    @Transactional
    @Override
    public void deleteStory(Long boardId) {
        Story story = storyRepository.findById(boardId)
                .orElseThrow(() -> new CustomPageException(BOARD_NOT_FOUND));
        if (!validateMember(boardId)) {
            throw new CustomPageException(CANNOT_TOUCH_NOT_YOURS);
        }
        storyRepository.delete(story);
    }
}
