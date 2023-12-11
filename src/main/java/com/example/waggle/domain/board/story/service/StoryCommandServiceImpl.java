package com.example.waggle.domain.board.story.service;

import com.example.waggle.domain.board.story.entity.Story;
import com.example.waggle.domain.board.story.repository.StoryRepository;
import com.example.waggle.domain.comment.entity.Comment;
import com.example.waggle.domain.comment.repository.CommentRepository;
import com.example.waggle.domain.comment.service.comment.CommentCommandService;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.recommend.entity.Recommend;
import com.example.waggle.domain.recommend.repository.RecommendRepository;
import com.example.waggle.global.exception.handler.MemberHandler;

import com.example.waggle.global.exception.handler.StoryHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.util.service.BoardType;
import com.example.waggle.global.util.service.UtilService;
import com.example.waggle.web.dto.story.StoryRequest;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class StoryCommandServiceImpl implements StoryCommandService{

    private final StoryRepository storyRepository;
    private final RecommendRepository recommendRepository;
    private final CommentRepository commentRepository;
    private final CommentCommandService commentCommandService;
    private final UtilService utilService;

    @Override
    public Long createStory(StoryRequest.Post request) throws IOException {
        Member member = utilService.getSignInMember();

        Story createdStory = Story.builder()
                .member(member)
                .content(request.getContent())
                .thumbnail(request.getThumbnail())
                .build();

        Story story = storyRepository.save(createdStory);

        if (!request.getHashtags().isEmpty()) {
            request.getHashtags().stream().forEach(h -> utilService.saveHashtag(story,h));
        }
//        mediaService.createMedias(story.getId(), multipartFiles, STORY);
        return story.getId();
    }

    @Override
    public Long updateStory(Long boardId,
                            StoryRequest.Post storyWriteDto) throws IOException {
        Story story = storyRepository.findById(boardId)
                .orElseThrow(() -> new StoryHandler(ErrorStatus.BOARD_NOT_FOUND));

        if(storyWriteDto.getThumbnail() != null)story.changeThumbnail(storyWriteDto.getThumbnail());
        story.changeContent(storyWriteDto.getContent());

        story.getBoardHashtags().clear();
        for (String hashtag : storyWriteDto.getHashtags()) {
            utilService.saveHashtag(story, hashtag);
        }
        return story.getId();
    }


    @Override
    public void deleteStory(Long boardId) {
        if (!utilService.validateMemberUseBoard(boardId, BoardType.STORY)) {
            throw new StoryHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Story story = storyRepository.findById(boardId)
                .orElseThrow(() -> new StoryHandler(ErrorStatus.BOARD_NOT_FOUND));

        List<Comment> comments = commentRepository.findByBoardId(story.getId());
        comments.stream().forEach(c -> commentCommandService.deleteComment(c.getId()));

        List<Recommend> recommends = recommendRepository.findByBoardId(story.getId());
        recommends.stream().forEach(r -> recommendRepository.delete(r));
        storyRepository.delete(story);
    }

}
