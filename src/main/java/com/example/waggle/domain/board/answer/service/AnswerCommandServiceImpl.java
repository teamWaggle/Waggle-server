package com.example.waggle.domain.board.answer.service;

import com.example.waggle.domain.board.answer.entity.Answer;
import com.example.waggle.domain.board.answer.repository.AnswerRepository;
import com.example.waggle.domain.board.question.entity.Question;
import com.example.waggle.domain.board.question.repository.QuestionRepository;
import com.example.waggle.domain.comment.entity.Comment;
import com.example.waggle.domain.comment.repository.CommentRepository;
import com.example.waggle.domain.comment.service.comment.CommentCommandService;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.recommend.entity.Recommend;
import com.example.waggle.domain.recommend.repository.RecommendRepository;
import com.example.waggle.global.exception.handler.AnswerHandler;
import com.example.waggle.global.exception.handler.QuestionHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.util.service.UtilService;
import com.example.waggle.web.dto.answer.AnswerRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

import static com.example.waggle.global.util.service.BoardType.ANSWER;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class AnswerCommandServiceImpl implements AnswerCommandService{
    //TODO user는 question 내에 하나의 answer만 사용할 수 있도록 제어

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final CommentRepository commentRepository;
    private final RecommendRepository recommendRepository;
    private final UtilService utilService;
    private final CommentCommandService commentCommandService;


    @Override
    public Long createAnswer(Long questionId,
                             AnswerRequest.Post answerWriteDto) throws IOException {
        Member signInMember = utilService.getSignInMember();
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionHandler(ErrorStatus.BOARD_NOT_FOUND));
        Answer answer = Answer.builder()
                .content(answerWriteDto.getContent())
                .member(signInMember)
                .question(question)
                .build();
        answerRepository.save(answer);
//        question.addAnswer(answer);

        //TODO save media
        if (!answerWriteDto.getHashtags().isEmpty()) {
            for (String hashtag : answerWriteDto.getHashtags()) {
                utilService.saveHashtag(answer, hashtag);
            }
        }
        return answer.getId();
    }

    @Override
    public Long updateAnswer(Long boardId,
                             AnswerRequest.Post answerWriteDto) throws IOException {
        Answer answer = answerRepository.findById(boardId)
                .orElseThrow(() -> new AnswerHandler(ErrorStatus.BOARD_NOT_FOUND));

        answer.changeAnswer(answerWriteDto.getContent());

        answer.getMedias().clear();
        answer.getBoardHashtags().clear();
        for (String hashtag : answerWriteDto.getHashtags()) {
            utilService.saveHashtag(answer, hashtag);
        }

        return answer.getId();
    }

    @Override
    public void deleteAnswer(Long boardId) {
        if (!utilService.validateMemberUseBoard(boardId, ANSWER)) {
            throw new AnswerHandler(ErrorStatus.CANNOT_TOUCH_NOT_YOURS);
        }
        Answer answer = answerRepository.findById(boardId)
                .orElseThrow(() -> new AnswerHandler(ErrorStatus.BOARD_NOT_FOUND));

        List<Comment> comments = commentRepository.findByBoardId(answer.getId());
        comments.stream().forEach(c -> commentCommandService.deleteComment(c.getId()));

        List<Recommend> recommends = recommendRepository.findByBoardId(answer.getId());
        recommends.stream().forEach(r -> recommendRepository.delete(r));

        answerRepository.delete(answer);
    }

}
