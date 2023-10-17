package com.example.waggle.board.story.service;

import com.example.waggle.board.story.domain.Story;
import com.example.waggle.board.story.dto.StoryDetailDto;
import com.example.waggle.board.story.dto.StorySummaryDto;
import com.example.waggle.board.story.dto.StoryWriteDto;
import com.example.waggle.board.story.repository.StoryRepository;
import com.example.waggle.commons.component.file.FileStore;
import com.example.waggle.commons.component.file.UploadFile;
import com.example.waggle.commons.exception.CustomPageException;
import com.example.waggle.commons.util.service.BoardType;
import com.example.waggle.commons.util.service.UtilService;
import com.example.waggle.media.domain.Media;
import com.example.waggle.media.service.MediaService;
import com.example.waggle.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.waggle.commons.exception.ErrorCode.BOARD_NOT_FOUND;
import static com.example.waggle.commons.exception.ErrorCode.CANNOT_TOUCH_NOT_YOURS;
import static com.example.waggle.commons.util.service.BoardType.*;


@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class StoryServiceImpl implements StoryService {

    private final StoryRepository storyRepository;
    private final UtilService utilService;
    private final MediaService mediaService;
    private final FileStore fileStore;


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

    @Transactional
    @Override
    public Long createStory(StoryWriteDto storyWriteDto, List<MultipartFile> multipartFiles, MultipartFile thumbnail) throws IOException {
        Member signInMember = utilService.getSignInMember();
        Story saveStory = storyWriteDto.toEntity(signInMember);
        Story story = storyRepository.save(saveStory);
        if(thumbnail != null) changeThumbnail(story, thumbnail);

        if (!storyWriteDto.getHashtags().isEmpty()) {
            for (String hashtagContent : storyWriteDto.getHashtags()) {
                utilService.saveHashtag(saveStory, hashtagContent);
            }
        }
        mediaService.createMedias(story.getId(), multipartFiles, STORY);

        return saveStory.getId();
    }

    @Transactional
    @Override
    public Long updateStory(Long boardId, StoryWriteDto storyWriteDto, List<MultipartFile> multipartFiles, MultipartFile thumbnail) throws IOException {
        Story story = storyRepository.findById(boardId)
                .orElseThrow(() -> new CustomPageException(BOARD_NOT_FOUND));

        if(thumbnail != null)changeThumbnail(story, thumbnail);
        story.changeContent(storyWriteDto.getContent());

        story.getMedias().clear();
        mediaService.createMedias(story.getId(), multipartFiles, STORY);

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

    public void changeThumbnail(Story story, MultipartFile thumbnail) throws IOException {
        UploadFile uploadFile = fileStore.storeFile(thumbnail);
        story.changeThumbnail(uploadFile.getStoreFileName());
    }
}
