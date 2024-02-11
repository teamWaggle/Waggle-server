package com.example.waggle.domain.board.story.service;

import com.example.waggle.domain.board.service.BoardService;
import com.example.waggle.domain.board.story.entity.Story;
import com.example.waggle.domain.board.story.repository.StoryRepository;
import com.example.waggle.domain.conversation.service.comment.CommentCommandService;
import com.example.waggle.domain.media.service.MediaCommandService;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.domain.recommend.repository.RecommendRepository;
import com.example.waggle.global.exception.handler.StoryHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.web.dto.media.MediaRequest;
import com.example.waggle.web.dto.story.StoryRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.example.waggle.domain.board.service.BoardType.STORY;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class StoryCommandServiceImpl implements StoryCommandService {

    private final StoryRepository storyRepository;
    private final RecommendRepository recommendRepository;
    private final MemberQueryService memberQueryService;
    private final BoardService boardService;
    private final MediaCommandService mediaCommandService;
    private final CommentCommandService commentCommandService;

    @Override
    public Long createStory(StoryRequest.Post request, List<MultipartFile> multipartFiles) throws IOException {
        Story createdStory = buildStory(request);

        Story story = storyRepository.save(createdStory);

        if (!request.getHashtags().isEmpty()) {
            request.getHashtags().stream().forEach(h -> boardService.saveHashtag(story, h));
        }
        boolean media = mediaCommandService.createMedia(multipartFiles, createdStory);
        return story.getId();
    }

    @Override
    public Long createStoryByUsername(StoryRequest.Post request, List<MultipartFile> multipartFiles, String username) throws IOException {
        Story createdStory = buildStory(request, username);

        Story story = storyRepository.save(createdStory);

        if (!request.getHashtags().isEmpty()) {
            request.getHashtags().stream().forEach(h -> boardService.saveHashtag(story, h));
        }
        boolean media = mediaCommandService.createMedia(multipartFiles, createdStory);
        return story.getId();

    }

    @Override
    public Long updateStory(Long boardId,
                            StoryRequest.Post storyWriteDto,
                            List<MultipartFile> multipartFiles,
                            List<String> deleteFile) throws IOException {
        if (!boardService.validateMemberUseBoard(boardId, STORY)) {
            throw new StoryHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Story story = storyRepository.findById(boardId)
                .orElseThrow(() -> new StoryHandler(ErrorStatus.BOARD_NOT_FOUND));


        story.changeContent(storyWriteDto.getContent());

        mediaCommandService.updateMedia(multipartFiles, deleteFile, story);
        story.getBoardHashtags().clear();
        for (String hashtag : storyWriteDto.getHashtags()) {
            boardService.saveHashtag(story, hashtag);
        }
        return story.getId();
    }

    @Override
    public Long updateStoryV2(Long boardId,
                              StoryRequest.Post storyWriteDto,
                              MediaRequest.Put mediaListDto,
                              List<MultipartFile> multipartFiles) throws IOException {
        if (!boardService.validateMemberUseBoard(boardId, STORY)) {
            throw new StoryHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Story story = storyRepository.findById(boardId)
                .orElseThrow(() -> new StoryHandler(ErrorStatus.BOARD_NOT_FOUND));

        story.changeContent(storyWriteDto.getContent());

        mediaCommandService.updateMediaV2(mediaListDto, multipartFiles, story);

        story.getBoardHashtags().clear();
        for (String hashtag : storyWriteDto.getHashtags()) {
            boardService.saveHashtag(story, hashtag);
        }
        return story.getId();
    }


    @Override
    public void deleteStory(Long boardId) {
        if (!boardService.validateMemberUseBoard(boardId, STORY)) {
            throw new StoryHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Story story = storyRepository.findById(boardId)
                .orElseThrow(() -> new StoryHandler(ErrorStatus.BOARD_NOT_FOUND));
        story.getComments().forEach(comment -> commentCommandService.deleteCommentForHardReset(comment.getId()));
        recommendRepository.deleteAllByBoardId(story.getId());

        storyRepository.delete(story);
    }

    private Story buildStory(StoryRequest.Post request) {
        Member member = memberQueryService.getSignInMember();
        Story createdStory = Story.builder()
                .member(member)
                .content(request.getContent())
                .build();
        return createdStory;
    }

    private Story buildStory(StoryRequest.Post request, String username) {
        Member member = memberQueryService.getMemberByUsername(username);
        Story createdStory = Story.builder()
                .member(member)
                .content(request.getContent())
                .build();
        return createdStory;
    }

}
