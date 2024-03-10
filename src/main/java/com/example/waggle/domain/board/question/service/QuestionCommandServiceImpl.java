package com.example.waggle.domain.board.question.service;

import com.example.waggle.domain.board.ResolutionStatus;
import com.example.waggle.domain.board.answer.entity.Answer;
import com.example.waggle.domain.board.answer.repository.AnswerRepository;
import com.example.waggle.domain.board.answer.service.AnswerCommandService;
import com.example.waggle.domain.board.question.entity.Question;
import com.example.waggle.domain.board.question.repository.QuestionRepository;
import com.example.waggle.domain.board.service.BoardService;
import com.example.waggle.domain.media.service.MediaCommandService;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.recommend.repository.RecommendRepository;
import com.example.waggle.global.exception.handler.QuestionHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.web.dto.media.MediaRequest.MediaUpdateDto;
import com.example.waggle.web.dto.question.QuestionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.example.waggle.domain.board.service.BoardType.QUESTION;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class QuestionCommandServiceImpl implements QuestionCommandService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final RecommendRepository recommendRepository;
    private final AnswerCommandService answerCommandService;
    private final BoardService boardService;
    private final MediaCommandService mediaCommandService;

    @Override
    public Long createQuestion(QuestionRequest createQuestionRequest,
                               List<MultipartFile> multipartFiles,
                               Member member) {
        Question createdQuestion = buildQuestion(createQuestionRequest, member);
        Question question = questionRepository.save(createdQuestion);

        for (String hashtag : createQuestionRequest.getHashtagList()) {
            boardService.saveHashtag(question, hashtag);
        }
        mediaCommandService.createMedia(multipartFiles, question);
        return question.getId();
    }

    @Override
    public Long updateQuestion(Long boardId,
                               QuestionRequest updateQuestionRequest,
                               MediaUpdateDto updateMediaRequest,
                               List<MultipartFile> multipartFiles,
                               Member member) {
        if (!boardService.validateMemberUseBoard(boardId, QUESTION, member)) {
            throw new QuestionHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Question question = questionRepository.findById(boardId)
                .orElseThrow(() -> new QuestionHandler(ErrorStatus.BOARD_NOT_FOUND));

        question.changeQuestion(updateQuestionRequest);
        mediaCommandService.updateMedia(updateMediaRequest, multipartFiles, question);

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

        List<Answer> answers = answerRepository.findAnswerByQuestionId(question.getId());
        answers.stream().forEach(answer -> answerCommandService.deleteAnswer(answer.getId(), member));
        recommendRepository.deleteAllByBoardId(question.getId());
        questionRepository.delete(question);
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
