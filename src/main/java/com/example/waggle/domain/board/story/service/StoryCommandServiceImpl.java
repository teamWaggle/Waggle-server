package com.example.waggle.domain.board.story.service;

import com.example.waggle.domain.board.story.entity.Story;
import com.example.waggle.domain.board.story.repository.StoryRepository;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.global.component.file.FileStore;
import com.example.waggle.global.component.file.UploadFile;
import com.example.waggle.global.exception.handler.MemberHandler;
import com.example.waggle.global.exception.handler.StoryHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.util.service.UtilService;
import com.example.waggle.web.dto.story.StoryWriteDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class StoryCommandServiceImpl implements StoryCommandService{

    private final StoryRepository storyRepository;
    private final UtilService utilService;
    private final FileStore fileStore;

    @Override
    public Long createStory(StoryWriteDto request,
                            List<MultipartFile> multipartFiles,
                            MultipartFile thumbnail) throws IOException {
        Member member = utilService.getSignInMember();

        Story createdStory = Story.builder()
                .member(member)
                .content(request.getContent())
                .thumbnail(request.getThumbnail())
                .build();

        Story story = storyRepository.save(createdStory);

        if(thumbnail != null) changeThumbnail(story, thumbnail);

        if (!request.getHashtags().isEmpty()) {
            for (String hashtagContent : request.getHashtags()) {
                utilService.saveHashtag(story, hashtagContent);
            }
        }
//        mediaService.createMedias(story.getId(), multipartFiles, STORY);

        return story.getId();
    }

    @Override
    public Long updateStory(Long boardId,
                            StoryWriteDto storyWriteDto,
                            List<MultipartFile> multipartFiles,
                            MultipartFile thumbnail) throws IOException {
        Story story = storyRepository.findById(boardId)
                .orElseThrow(() -> new StoryHandler(ErrorStatus.BOARD_NOT_FOUND));

        if(thumbnail != null)changeThumbnail(story, thumbnail);
        story.changeContent(storyWriteDto.getContent());

        story.getMedias().clear();
//        mediaService.createMedias(story.getId(), multipartFiles, STORY);

        story.getBoardHashtags().clear();
        for (String hashtag : storyWriteDto.getHashtags()) {
            utilService.saveHashtag(story, hashtag);
        }
        return story.getId();
    }


    @Override
    public StoryWriteDto getStoryWriteDtoByBoardId(Long boardId) {
        Story story = storyRepository.findById(boardId)
                .orElseThrow(() -> new StoryHandler(ErrorStatus.BOARD_NOT_FOUND));
        return StoryWriteDto.toDto(story);
    }

    @Override
    public boolean validateMember(Long boardId) {
        Member signInMember = utilService.getSignInMember();
        Story story = storyRepository.findById(boardId)
                .orElseThrow(() -> new StoryHandler(ErrorStatus.BOARD_NOT_FOUND));
        return story.getMember().equals(signInMember);
    }

    @Override
    public void deleteStory(Long boardId) {
        Story story = storyRepository.findById(boardId)
                .orElseThrow(() -> new StoryHandler(ErrorStatus.BOARD_NOT_FOUND));
        if (!validateMember(boardId)) {
            throw new MemberHandler(ErrorStatus.CANNOT_TOUCH_NOT_YOURS);
        }
        storyRepository.delete(story);
    }

    public void changeThumbnail(Story story, MultipartFile thumbnail) throws IOException {
        UploadFile uploadFile = fileStore.storeFile(thumbnail);
        story.changeThumbnail(uploadFile.getStoreFileName());
    }
}
