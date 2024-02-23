package com.example.waggle.domain.board.answer.service;

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
import com.example.waggle.web.dto.media.MediaRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.example.waggle.domain.board.service.BoardType.ANSWER;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class AnswerCommandServiceImpl implements AnswerCommandService {
    //TODO user는 question 내에 하나의 answer만 사용할 수 있도록 제어

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final RecommendRepository recommendRepository;
    private final MemberQueryService memberQueryService;
    private final CommentCommandService commentCommandService;
    private final BoardService boardService;
    private final MediaCommandService mediaCommandService;


    @Override
    public Long createAnswer(Long questionId,
                             AnswerRequest.Post request,
                             List<MultipartFile> multiPartFiles) {
        Member signInMember = memberQueryService.getSignInMember();
        Answer answer = buildAnswer(questionId, request, signInMember);
        answerRepository.save(answer);

        mediaCommandService.createMedia(multiPartFiles, answer);
        return answer.getId();
    }

    @Override
    public Long createAnswer(Long questionId,
                             Member member,
                             AnswerRequest.Post request,
                             List<MultipartFile> multipartFiles) {
        Answer answer = buildAnswer(questionId, request, member);
        answerRepository.save(answer);

        mediaCommandService.createMedia(multipartFiles, answer);
        return answer.getId();
    }


    @Override
    public Long updateAnswer(Long boardId,
                             AnswerRequest.Post request,
                             List<MultipartFile> multipartFiles,
                             List<String> deleteFiles) {
        Answer answer = answerRepository.findById(boardId)
                .orElseThrow(() -> new AnswerHandler(ErrorStatus.BOARD_NOT_FOUND));

        answer.changeAnswer(request.getContent());
        mediaCommandService.updateMedia(multipartFiles, deleteFiles, answer);
        return answer.getId();
    }

    @Override
    public Long updateAnswerV2(Long boardId,
                               AnswerRequest.Post request,
                               MediaRequest.Put mediaUpdateDto,
                               List<MultipartFile> multipartFiles) {
        if (!boardService.validateMemberUseBoard(boardId, ANSWER)) {
            throw new AnswerHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Answer answer = answerRepository.findById(boardId)
                .orElseThrow(() -> new AnswerHandler(ErrorStatus.BOARD_NOT_FOUND));


        answer.changeAnswer(request.getContent());

        mediaCommandService.updateMediaV2(mediaUpdateDto, multipartFiles, answer);

        return answer.getId();
    }

    @Override
    public Long updateAnswer(Long boardId,
                             Member member,
                             AnswerRequest.Post request,
                             MediaRequest.Put mediaUpdateDto,
                             List<MultipartFile> multipartFiles) {
        if (!boardService.validateMemberUseBoard(boardId, ANSWER, member)) {
            throw new AnswerHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Answer answer = answerRepository.findById(boardId)
                .orElseThrow(() -> new AnswerHandler(ErrorStatus.BOARD_NOT_FOUND));

        answer.changeAnswer(request.getContent());
        mediaCommandService.updateMediaV2(mediaUpdateDto, multipartFiles, answer);

        return answer.getId();
    }


    @Override
    public void deleteAnswer(Long boardId) {
        if (!boardService.validateMemberUseBoard(boardId, ANSWER)) {
            throw new AnswerHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Answer answer = answerRepository.findById(boardId)
                .orElseThrow(() -> new AnswerHandler(ErrorStatus.BOARD_NOT_FOUND));

        answer.getComments().forEach(comment -> commentCommandService.deleteCommentForHardReset(comment.getId()));
        recommendRepository.deleteAllByBoardId(boardId);

        answerRepository.delete(answer);
    }

    @Override
    public void deleteAnswer(Long boardId, Member member) {
        if (!boardService.validateMemberUseBoard(boardId, ANSWER, member)) {
            throw new AnswerHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Answer answer = answerRepository.findById(boardId)
                .orElseThrow(() -> new AnswerHandler(ErrorStatus.BOARD_NOT_FOUND));

        answer.getComments().forEach(comment -> commentCommandService.deleteCommentForHardReset(comment.getId()));
        recommendRepository.deleteAllByBoardId(boardId);

        answerRepository.delete(answer);
    }

    private Answer buildAnswer(Long questionId, AnswerRequest.Post answerWriteDto, Member signInMember) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionHandler(ErrorStatus.BOARD_NOT_FOUND));
        Answer answer = Answer.builder()
                .content(answerWriteDto.getContent())
                .member(signInMember)
                .question(question)
                .build();
        return answer;
    }
}
