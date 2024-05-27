package com.example.waggle.domain.board.application.story;

import com.example.waggle.domain.board.application.board.BoardService;
import com.example.waggle.domain.board.persistence.dao.board.jpa.BoardRepository;
import com.example.waggle.domain.board.persistence.dao.story.jpa.StoryRepository;
import com.example.waggle.domain.board.persistence.entity.Story;
import com.example.waggle.domain.board.presentation.dto.story.StoryRequest;
import com.example.waggle.domain.conversation.application.comment.CommentCommandService;
import com.example.waggle.domain.conversation.persistence.dao.comment.jpa.CommentRepository;
import com.example.waggle.domain.media.application.MediaCommandService;
import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.member.persistence.entity.Role;
import com.example.waggle.domain.recommend.persistence.dao.RecommendRepository;
import com.example.waggle.exception.object.handler.MemberHandler;
import com.example.waggle.exception.object.handler.StoryHandler;
import com.example.waggle.exception.payload.code.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.waggle.domain.board.persistence.entity.BoardType.STORY;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class StoryCommandServiceImpl implements StoryCommandService {

    private final StoryRepository storyRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final RecommendRepository recommendRepository;
    private final BoardService boardService;
    private final MediaCommandService mediaCommandService;
    private final CommentCommandService commentCommandService;


    @Override
    public Long createStory(StoryRequest createStoryRequest,
                            Member member) {
        Story createdStory = buildStory(createStoryRequest, member);
        Story story = storyRepository.save(createdStory);

        if (!createStoryRequest.getHashtagList().isEmpty()) {
            createStoryRequest.getHashtagList().stream().forEach(h -> boardService.saveHashtag(story, h));
        }
        mediaCommandService.createMedia(createStoryRequest.getMediaList(), story);
        return story.getId();
    }

    @Override
    public Long updateStory(Long boardId,
                            StoryRequest updateStoryRequest,
                            Member member) {
        if (!boardService.validateMemberUseBoard(boardId, STORY, member)) {
            throw new StoryHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Story story = storyRepository.findById(boardId)
                .orElseThrow(() -> new StoryHandler(ErrorStatus.BOARD_NOT_FOUND));

        story.changeContent(updateStoryRequest.getContent());
        mediaCommandService.updateMedia(updateStoryRequest.getMediaList(), story);

        story.getBoardHashtags().clear();
        for (String hashtag : updateStoryRequest.getHashtagList()) {
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

    @Override
    public void deleteStoryWithRelations(Long boardId, Member member) {
        if (!boardService.validateMemberUseBoard(boardId, STORY, member)) {
            throw new StoryHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        commentRepository.deleteCommentsWithRelationsByBoard(boardId);
        boardRepository.deleteBoardsWithRelations(STORY, boardId);
    }

    @Override
    public void deleteStoryByAdmin(Long boardId, Member member) {
        if (!member.getRole().equals(Role.ADMIN)) {
            throw new MemberHandler(ErrorStatus.MEMBER_ACCESS_DENIED_BY_AUTHORIZATION);
        }
        commentRepository.deleteCommentsWithRelationsByBoard(boardId);
        boardRepository.deleteBoardsWithRelations(STORY, boardId);
    }

    private Story buildStory(StoryRequest createStoryRequest, Member member) {
        return Story.builder()
                .member(member)
                .content(createStoryRequest.getContent())
                .build();
    }

}
