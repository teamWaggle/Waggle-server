package com.example.waggle.domain.board.application.question;

import com.example.waggle.domain.board.application.board.BoardService;
import com.example.waggle.domain.board.persistence.dao.question.jpa.QuestionRepository;
import com.example.waggle.domain.board.persistence.entity.Question;
import com.example.waggle.domain.board.persistence.entity.ResolutionStatus;
import com.example.waggle.domain.board.presentation.dto.question.QuestionRequest;
import com.example.waggle.domain.conversation.persistence.dao.comment.jpa.CommentRepository;
import com.example.waggle.domain.media.application.MediaCommandService;
import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.recommend.persistence.dao.RecommendRepository;
import com.example.waggle.exception.object.handler.QuestionHandler;
import com.example.waggle.exception.payload.code.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.waggle.domain.board.persistence.entity.BoardType.QUESTION;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class QuestionCommandServiceImpl implements QuestionCommandService {

    private final QuestionRepository questionRepository;
    private final RecommendRepository recommendRepository;
    private final CommentRepository commentRepository;
    private final BoardService boardService;
    private final MediaCommandService mediaCommandService;

    @Override
    public Long createQuestion(QuestionRequest createQuestionRequest,
                               Member member) {
        Question createdQuestion = buildQuestion(createQuestionRequest, member);
        Question question = questionRepository.save(createdQuestion);

        for (String hashtag : createQuestionRequest.getHashtagList()) {
            boardService.saveHashtag(question, hashtag);
        }
        mediaCommandService.createMedia(createQuestionRequest.getMediaList(), question);
        return question.getId();
    }

    @Override
    public Long updateQuestion(Long boardId,
                               QuestionRequest updateQuestionRequest,
                               Member member) {
        if (!boardService.validateMemberUseBoard(boardId, QUESTION, member)) {
            throw new QuestionHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Question question = questionRepository.findById(boardId)
                .orElseThrow(() -> new QuestionHandler(ErrorStatus.BOARD_NOT_FOUND));

        question.changeQuestion(updateQuestionRequest);
        mediaCommandService.updateMedia(updateQuestionRequest.getMediaList(), question);

        question.getBoardHashtags().clear();
        for (String hashtag : updateQuestionRequest.getHashtagList()) {
            boardService.saveHashtag(question, hashtag);
        }
        return question.getId();
    }


    @Override
    public void convertStatus(Long boardId, Member member) {
        if (!boardService.validateMemberUseBoard(boardId, QUESTION, member)) {
            throw new QuestionHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Question question = questionRepository.findById(boardId)
                .orElseThrow(() -> new QuestionHandler(ErrorStatus.BOARD_NOT_FOUND));
        switch (question.getStatus()) {
            case RESOLVED -> question.changeStatus(ResolutionStatus.UNRESOLVED);
            case UNRESOLVED -> question.changeStatus(ResolutionStatus.RESOLVED);
            default -> throw new QuestionHandler(ErrorStatus.BOARD_INVALID_TYPE);
        }
    }

    @Override
    public void deleteQuestion(Long boardId, Member member) {
        if (!boardService.validateMemberUseBoard(boardId, QUESTION, member)) {
            throw new QuestionHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Question question = questionRepository.findById(boardId)
                .orElseThrow(() -> new QuestionHandler(ErrorStatus.BOARD_NOT_FOUND));

        recommendRepository.deleteAllByBoardId(question.getId());
        questionRepository.delete(question);
    }

    @Override
    public void deleteQuestionWithRelations(Long boardId, Member member) {
        if (!boardService.validateMemberUseBoard(boardId, QUESTION, member)) {
            throw new QuestionHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        commentRepository.deleteCommentsWithRelationsByBoard(boardId);
        questionRepository.deleteQuestionWithRelations(boardId);
    }

    private Question buildQuestion(QuestionRequest createQuestionRequest, Member member) {
        return Question.builder()
                .title(createQuestionRequest.getTitle())
                .content(createQuestionRequest.getContent())
                .status(ResolutionStatus.UNRESOLVED)
                .member(member)
                .build();
    }
}
