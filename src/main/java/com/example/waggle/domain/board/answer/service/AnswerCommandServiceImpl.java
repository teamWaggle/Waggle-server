package com.example.waggle.domain.board.answer.service;

import static com.example.waggle.domain.board.service.BoardType.ANSWER;

import com.example.waggle.domain.board.answer.entity.Answer;
import com.example.waggle.domain.board.answer.repository.AnswerRepository;
import com.example.waggle.domain.board.question.entity.Question;
import com.example.waggle.domain.board.question.repository.QuestionRepository;
import com.example.waggle.domain.board.service.BoardService;
import com.example.waggle.domain.conversation.service.comment.CommentCommandService;
import com.example.waggle.domain.media.service.MediaCommandService;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.domain.recommend.repository.RecommendRepository;
import com.example.waggle.global.exception.handler.AnswerHandler;
import com.example.waggle.global.exception.handler.QuestionHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.web.dto.answer.AnswerRequest;
import com.example.waggle.web.dto.media.MediaRequest.MediaUpdateDto;
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
public class AnswerCommandServiceImpl implements AnswerCommandService {
    //TODO user는 question 내에 하나의 answer만 사용할 수 있도록 제어

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final RecommendRepository recommendRepository;
    private final CommentCommandService commentCommandService;
    private final BoardService boardService;
    private final MediaCommandService mediaCommandService;

    @Override
    public Long createAnswer(Long questionId,
                             AnswerRequest createAnswerRequest,
                             List<MultipartFile> multipartFiles,
                             Member member) {
        Answer answer = buildAnswer(questionId, createAnswerRequest, member);
        answerRepository.save(answer);
        mediaCommandService.createMedia(multipartFiles, answer);
        return answer.getId();
    }

    @Override
    public Long updateAnswer(Long boardId,
                             AnswerRequest updateAnswerRequest,
                             MediaUpdateDto updateMediaRequest,
                             List<MultipartFile> multipartFiles,
                             Member member) {
        if (!boardService.validateMemberUseBoard(boardId, ANSWER, member)) {
            throw new AnswerHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Answer answer = answerRepository.findById(boardId)
                .orElseThrow(() -> new AnswerHandler(ErrorStatus.BOARD_NOT_FOUND));

        answer.changeAnswer(updateAnswerRequest.getContent());
        mediaCommandService.updateMediaV2(updateMediaRequest, multipartFiles, answer);

        return answer.getId();
    }

    @Override
    public void deleteAnswer(Long boardId, Member member) {
        if (!boardService.validateMemberUseBoard(boardId, ANSWER, member)) {
            throw new AnswerHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Answer answer = answerRepository.findById(boardId)
                .orElseThrow(() -> new AnswerHandler(ErrorStatus.BOARD_NOT_FOUND));

        answer.getComments()
                .forEach(comment -> commentCommandService.deleteCommentForHardReset(comment.getId()));
        recommendRepository.deleteAllByBoardId(boardId);
        answerRepository.delete(answer);
    }

    private Answer buildAnswer(Long questionId, AnswerRequest createAnswerRequest, Member member) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionHandler(ErrorStatus.BOARD_NOT_FOUND));
        return Answer.builder()
                .content(createAnswerRequest.getContent())
                .member(member)
                .question(question)
                .build();
    }
}
