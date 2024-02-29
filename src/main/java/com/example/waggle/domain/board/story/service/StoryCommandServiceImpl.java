package com.example.waggle.domain.board.story.service;

import static com.example.waggle.domain.board.service.BoardType.STORY;

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
import com.example.waggle.web.dto.media.MediaRequest.MediaUpdateDto;
import com.example.waggle.web.dto.story.StoryRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    public Long createStory(StoryRequest createStoryRequest, List<MultipartFile> multipartFiles,
                            Member member) {
        Story createdStory = buildStory(createStoryRequest, member);
        Story story = storyRepository.save(createdStory);

        if (!createStoryRequest.getHashtagList().isEmpty()) {
            createStoryRequest.getHashtagList().stream().forEach(h -> boardService.saveHashtag(story, h));
        }
        boolean media = mediaCommandService.createMedia(multipartFiles, createdStory);
        return story.getId();
    }

    @Override
    public Long updateStory(Long boardId,
                            StoryRequest createStoryRequest,
                            MediaUpdateDto updateMediaRequest,
                            List<MultipartFile> multipartFiles,
                            Member member) {
        if (!boardService.validateMemberUseBoard(boardId, STORY, member)) {
            throw new StoryHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Story story = storyRepository.findById(boardId)
                .orElseThrow(() -> new StoryHandler(ErrorStatus.BOARD_NOT_FOUND));

        story.changeContent(createStoryRequest.getContent());
        mediaCommandService.updateMediaV2(updateMediaRequest, multipartFiles, story);

        story.getBoardHashtags().clear();
        for (String hashtag : createStoryRequest.getHashtagList()) {
            boardService.saveHashtag(story, hashtag);
        }
        return story.getId();
    }

    @Override
    public void deleteStory(Long boardId, Member member) {
        if (!boardService.validateMemberUseBoard(boardId, STORY, member)) {
            throw new StoryHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Story story = storyRepository.findById(boardId)
                .orElseThrow(() -> new StoryHandler(ErrorStatus.BOARD_NOT_FOUND));

        story.getComments().forEach(comment -> commentCommandService.deleteCommentForHardReset(comment.getId()));
        recommendRepository.deleteAllByBoardId(story.getId());
        storyRepository.delete(story);
    }

    private Story buildStory(StoryRequest createStoryRequest, Member member) {
        return Story.builder()
                .member(member)
                .content(createStoryRequest.getContent())
                .build();
    }

}
