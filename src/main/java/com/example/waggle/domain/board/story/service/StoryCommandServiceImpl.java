package com.example.waggle.domain.board.story.service;

import com.example.waggle.domain.board.story.entity.Story;
import com.example.waggle.domain.board.story.repository.StoryRepository;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.global.exception.handler.MemberHandler;
import com.example.waggle.global.exception.handler.StoryHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.util.service.BoardType;
import com.example.waggle.global.util.service.UtilService;
import com.example.waggle.web.dto.story.StoryRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class StoryCommandServiceImpl implements StoryCommandService{

    private final StoryRepository storyRepository;
    private final UtilService utilService;

    @Override
    public Long createStory(StoryRequest.Post request,
                            List<String> multipartFiles,
                            String thumbnail) throws IOException {
        Member member = utilService.getSignInMember();

        Story createdStory = Story.builder()
                .member(member)
                .content(request.getContent())
                .thumbnail(thumbnail)
                .build();

        Story story = storyRepository.save(createdStory);

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
                            StoryRequest.Post storyWriteDto,
                            List<String> uploadFiles,
                            String thumbnail) throws IOException {
        Story story = storyRepository.findById(boardId)
                .orElseThrow(() -> new StoryHandler(ErrorStatus.BOARD_NOT_FOUND));

        if(thumbnail != null)story.changeThumbnail(thumbnail);
        story.changeContent(storyWriteDto.getContent());
//        story.getMedias().clear();
//        mediaService.createMedias(story.getId(), multipartFiles, STORY);

        story.getBoardHashtags().clear();
        for (String hashtag : storyWriteDto.getHashtags()) {
            utilService.saveHashtag(story, hashtag);
        }
        return story.getId();
    }


    @Override
    public void deleteStory(Long boardId) {
        Story story = storyRepository.findById(boardId)
                .orElseThrow(() -> new StoryHandler(ErrorStatus.BOARD_NOT_FOUND));
        if (!utilService.validateMemberUseBoard(boardId, BoardType.STORY)) {
            throw new MemberHandler(ErrorStatus.CANNOT_TOUCH_NOT_YOURS);
        }
        storyRepository.delete(story);
    }

}
